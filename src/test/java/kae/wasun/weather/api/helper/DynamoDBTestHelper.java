package kae.wasun.weather.api.helper;

import kae.wasun.weather.api.model.document.WeatherTrackingDocument;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.Key;

public class DynamoDBTestHelper {

    private DynamoDBTestHelper() {

    }

    public static WeatherTrackingDocument getItem(DynamoDbEnhancedClient enhancedClient,
                                                  String partitionKey,
                                                  String sortKey) {
        var dynamoDbTable = enhancedClient.table(
                WeatherTrackingDocument.TABLE_NAME,
                WeatherTrackingDocument.TABLE_SCHEMA
        );

        var key = Key.builder()
                .partitionValue(partitionKey)
                .sortValue(sortKey)
                .build();

        return dynamoDbTable.getItem(key);
    }

    public static void putItem(DynamoDbEnhancedClient enhancedClient, WeatherTrackingDocument document) {
        var dynamoDbTable = enhancedClient.table(
                WeatherTrackingDocument.TABLE_NAME,
                WeatherTrackingDocument.TABLE_SCHEMA
        );

        dynamoDbTable.putItem(document);
    }

    public static void deleteAllItems(DynamoDbEnhancedClient enhancedClient) {
        var dynamoDbTable = enhancedClient.table(
                WeatherTrackingDocument.TABLE_NAME,
                WeatherTrackingDocument.TABLE_SCHEMA
        );

        var pageIterable = dynamoDbTable.scan();

        pageIterable.stream().forEach(page ->
                page.items().forEach(dynamoDbTable::deleteItem)
        );
    }
}
