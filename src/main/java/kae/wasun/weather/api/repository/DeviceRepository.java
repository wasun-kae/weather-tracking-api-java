package kae.wasun.weather.api.repository;

import kae.wasun.weather.api.model.document.WeatherTrackingDocument;
import kae.wasun.weather.api.model.domain.Device;
import kae.wasun.weather.api.model.exception.ItemAlreadyExistsException;
import kae.wasun.weather.api.util.ClockUtil;
import kae.wasun.weather.api.util.TimeFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.Optional;

@Repository
public class DeviceRepository extends AbstractDocumentRepository {

    private final ClockUtil clockUtil;

    public DeviceRepository(@Autowired DynamoDbEnhancedClient dynamoDbEnhancedClient,
                            @Autowired ClockUtil clockUtil) {
        
        super(dynamoDbEnhancedClient);
        this.clockUtil = clockUtil;
    }

    public Optional<Device> findById(String id) {
        var partitionKey = MessageFormat.format("device#{0}", id);
        var sortKey = MessageFormat.format("device#{0}", id);
        var documents = queryDocuments(partitionKey, sortKey, false);

        return documents.isEmpty() ? Optional.empty() : Optional.of(
                this.convertToDevice(documents.get(0))
        );
    }

    public Device create(Device device) throws ItemAlreadyExistsException {
        var partitionKey = MessageFormat.format("device#{0}", device.getId());
        var sortKey = MessageFormat.format("device#{0}", device.getId());
        var currentDateTime = clockUtil.getCurrentTime();
        var formattedCurrentDateTime = TimeFormatUtil.convertToString(currentDateTime);

        var documentToCreate = WeatherTrackingDocument.builder()
                .PK(partitionKey)
                .SK(sortKey)
                .id(device.getId())
                .createdAt(formattedCurrentDateTime)
                .build();

        var expression = Expression.builder()
                .expression("attribute_not_exists(PK) AND attribute_not_exists(SK)")
                .build();

        var putItemRequest = PutItemEnhancedRequest.builder(WeatherTrackingDocument.class)
                .item(documentToCreate)
                .conditionExpression(expression)
                .build();

        try {
            super.dynamoDbTable.putItem(putItemRequest);
        } catch (ConditionalCheckFailedException exception) {
            throw new ItemAlreadyExistsException();
        }

        var documents = super.queryDocuments(partitionKey, sortKey, true);

        return this.convertToDevice(documents.get(0));
    }

    private Device convertToDevice(WeatherTrackingDocument document) {
        return Device.builder()
                .id(document.getId())
                .createdAt(Instant.parse(document.getCreatedAt()))
                .build();
    }
}
