package kae.wasun.weather.api.repository;

import kae.wasun.weather.api.model.document.WeatherTrackingDocument;
import kae.wasun.weather.api.model.domain.DeviceTracking;
import kae.wasun.weather.api.model.exception.ItemAlreadyExistsException;
import kae.wasun.weather.api.util.ClockUtil;
import kae.wasun.weather.api.util.TimeFormatUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

import java.text.MessageFormat;

@Repository
public class DeviceTrackingRepository {

    private final DynamoDbTable<WeatherTrackingDocument> dynamoDbTable;
    private final ClockUtil clockUtil;

    public DeviceTrackingRepository(@Autowired DynamoDbEnhancedClient dynamoDbEnhancedClient,
                                    @Autowired ClockUtil clockUtil) {

        this.clockUtil = clockUtil;
        this.dynamoDbTable = dynamoDbEnhancedClient.table(
                WeatherTrackingDocument.TABLE_NAME,
                WeatherTrackingDocument.TABLE_SCHEMA
        );
    }

    public DeviceTracking create(String deviceId, DeviceTracking deviceTracking) throws ItemAlreadyExistsException {
        var formattedTimestamp = TimeFormatUtil.convertToString(deviceTracking.getTimestamp());

        var currentDateTime = clockUtil.getCurrentTime();
        var formattedCurrentDateTime = TimeFormatUtil.convertToString(currentDateTime);

        var partitionKey = MessageFormat.format("device#{0}", deviceId);
        var sortKey = MessageFormat.format("timestamp#{0}", formattedTimestamp);
        var documentToCreate = WeatherTrackingDocument.builder()
                .PK(partitionKey)
                .SK(sortKey)
                .timestamp(formattedTimestamp)
                .temperature(deviceTracking.getTemperature())
                .humidity(deviceTracking.getHumidity())
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
            dynamoDbTable.putItem(putItemRequest);
        } catch (ConditionalCheckFailedException exception) {
            throw new ItemAlreadyExistsException();
        }

        return null;
    }
}
