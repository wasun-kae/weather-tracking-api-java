package kae.wasun.weather.api.helper;

import kae.wasun.weather.api.model.document.WeatherTrackingDocument;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

public class DynamoDBTestHelper {

    private DynamoDBTestHelper() {

    }

    private static DynamoDbTable<WeatherTrackingDocument> getDynamoDBTable(DynamoDbEnhancedClient enhancedClient) {
        return enhancedClient.table(
                WeatherTrackingDocument.TABLE_NAME,
                WeatherTrackingDocument.TABLE_SCHEMA
        );
    }

    public static WeatherTrackingDocument getItem(DynamoDbEnhancedClient enhancedClient,
                                                  String partitionKey,
                                                  String sortKey) {
        var dynamoDbTable = getDynamoDBTable(enhancedClient);
        var key = Key.builder()
                .partitionValue(partitionKey)
                .sortValue(sortKey)
                .build();

        return dynamoDbTable.getItem(key);
    }

    public static void putItem(DynamoDbEnhancedClient enhancedClient, WeatherTrackingDocument document) {
        var dynamoDbTable = getDynamoDBTable(enhancedClient);
        dynamoDbTable.putItem(document);
    }

    public static void deleteAllItems(DynamoDbEnhancedClient enhancedClient) {
        var dynamoDbTable = getDynamoDBTable(enhancedClient);
        var pageIterable = dynamoDbTable.scan();

        pageIterable.stream().forEach(page ->
                page.items().forEach(dynamoDbTable::deleteItem)
        );
    }
}
