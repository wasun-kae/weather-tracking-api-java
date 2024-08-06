package kae.wasun.weather.api.repository;

import kae.wasun.weather.api.model.document.WeatherTrackingDocument;
import kae.wasun.weather.api.model.domain.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.GetItemEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;

import java.text.MessageFormat;
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
        var compositeKeys = Key.builder()
                .partitionValue(partitionKey)
                .sortValue(sortKey)
                .build();

        var document = dynamoDbTable.getItem(compositeKeys);

        return document == null ? Optional.empty() : Optional.of(
                Device.builder()
                        .id(document.getId())
                        .build()
        );
    }

    public Device save(Device device) {
        var partitionKey = MessageFormat.format("device#{0}", device.getId());
        var sortKey = MessageFormat.format("device#{0}", device.getId());
        var documentToSaved = WeatherTrackingDocument.builder()
                .PK(partitionKey)
                .SK(sortKey)
                .id(device.getId())
                .build();

        var expression = Expression.builder()
                .expression("attribute_not_exists(PK) AND attribute_not_exists(SK)")
                .build();

        var putItemRequest = PutItemEnhancedRequest.builder(WeatherTrackingDocument.class)
                .item(documentToSaved)
                .conditionExpression(expression)
                .build();

        dynamoDbTable.putItem(putItemRequest);

        var compositeKeys = Key.builder()
                .partitionValue(partitionKey)
                .sortValue(sortKey)
                .build();

        var getItemRequest = GetItemEnhancedRequest.builder()
                .key(compositeKeys)
                .consistentRead(true)
                .build();

        var savedDocument = dynamoDbTable.getItem(getItemRequest);

        return Device.builder()
                .id(savedDocument.getId())
                .build();
    }
}
