package kae.wasun.weather.api.repository;

import kae.wasun.weather.api.model.document.WeatherTrackingDocument;
import kae.wasun.weather.api.model.domain.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

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

        var weatherTracking = dynamoDbTable.getItem(compositeKeys);
        
        return weatherTracking == null ? Optional.empty() :
                Optional.of(Device.builder()
                        .id(weatherTracking.getId())
                        .build()
                );
    }
}
