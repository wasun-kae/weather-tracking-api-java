package kae.wasun.weather.api.repository;

import kae.wasun.weather.api.model.document.WeatherTrackingDocument;
import kae.wasun.weather.api.model.domain.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public class DeviceRepository {

    private final DynamoDbTable<WeatherTrackingDocument> dynamoDbTable;

    public DeviceRepository(@Autowired DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.dynamoDbTable = dynamoDbEnhancedClient.table(
                WeatherTrackingDocument.TABLE_NAME,
                WeatherTrackingDocument.TABLE_SCHEMA
        );
    }

    public Optional<Device> findById(String id) {
        var partitionKey = MessageFormat.format("device#{0}", id);
        var sortKey = MessageFormat.format("device#{0}", id);
        var documents = queryDocuments(partitionKey, sortKey, false);

        return documents.isEmpty() ? Optional.empty() : Optional.of(
                Device.builder()
                        .id(documents.get(0).getId())
                        .build()
        );
    }

    public Device create(Device device) {
        var partitionKey = MessageFormat.format("device#{0}", device.getId());
        var sortKey = MessageFormat.format("device#{0}", device.getId());
        var documentToCreate = WeatherTrackingDocument.builder()
                .PK(partitionKey)
                .SK(sortKey)
                .id(device.getId())
                .build();

        var expression = Expression.builder()
                .expression("attribute_not_exists(PK) AND attribute_not_exists(SK)")
                .build();

        var putItemRequest = PutItemEnhancedRequest.builder(WeatherTrackingDocument.class)
                .item(documentToCreate)
                .conditionExpression(expression)
                .build();

        dynamoDbTable.putItem(putItemRequest);

        var documents = queryDocuments(partitionKey, sortKey, true);

        return Device.builder()
                .id(documents.get(0).getId())
                .build();
    }

    private List<WeatherTrackingDocument> queryDocuments(String partitionKey, String sortKey, boolean consistentRead) {
        var compositeKeys = Key.builder()
                .partitionValue(partitionKey)
                .sortValue(sortKey)
                .build();

        var queryConditional = QueryConditional.keyEqualTo(compositeKeys);
        var queryRequest = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .consistentRead(consistentRead)
                .build();

        var pageIterable = dynamoDbTable.query(queryRequest);

        return pageIterable.stream()
                .map(Page::items)
                .flatMap(Collection::stream)
                .toList();
    }
}
