package kae.wasun.weather.api.config;

import kae.wasun.weather.api.model.document.WeatherTrackingDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.testcontainers.containers.localstack.LocalStackContainer;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@TestConfiguration
public class DynamoDBConfig {

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(@Autowired AwsCredentialsProvider awsCredentialsProvider,
                                                         @Autowired LocalStackContainer localStackContainer) {
        var region = Region.of(localStackContainer.getRegion());
        var endpoint = localStackContainer.getEndpointOverride(LocalStackContainer.Service.DYNAMODB);

        var dynamoDbClient = DynamoDbClient.builder()
                .credentialsProvider(awsCredentialsProvider)
                .region(region)
                .endpointOverride(endpoint)
                .build();

        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }

    @EventListener
    public void onApplicationReady(ApplicationReadyEvent applicationReadyEvent) {
        var applicationContext = applicationReadyEvent.getApplicationContext();
        var dynamoDbEnhancedClient = applicationContext.getBean(
                "dynamoDbEnhancedClient",
                DynamoDbEnhancedClient.class
        );

        var weatherTrackingTable = dynamoDbEnhancedClient.table(
                WeatherTrackingDocument.TABLE_NAME,
                WeatherTrackingDocument.TABLE_SCHEMA
        );

        weatherTrackingTable.createTable();
    }
}
