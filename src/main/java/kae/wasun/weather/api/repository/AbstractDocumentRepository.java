package kae.wasun.weather.api.repository;

import kae.wasun.weather.api.model.document.WeatherTrackingDocument;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.Collection;
import java.util.List;

public abstract class AbstractDocumentRepository {

    protected final DynamoDbTable<WeatherTrackingDocument> dynamoDbTable;

    protected AbstractDocumentRepository(DynamoDbEnhancedClient dynamoDbEnhancedClient) {
        this.dynamoDbTable = dynamoDbEnhancedClient.table(
                WeatherTrackingDocument.TABLE_NAME,
                WeatherTrackingDocument.TABLE_SCHEMA
        );
    }

    protected List<WeatherTrackingDocument> queryDocuments(String partitionKey,
                                                           String sortKey,
                                                           boolean consistentRead) {
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
