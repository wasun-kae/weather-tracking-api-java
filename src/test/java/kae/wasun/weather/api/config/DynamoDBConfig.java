package kae.wasun.weather.api.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.localstack.LocalStackContainer;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@TestConfiguration
public class DynamoDBConfig {

    @Bean
    public DynamoDbClient dynamoDbClient(AwsCredentialsProvider awsCredentialsProvider,
                                         LocalStackContainer localStackContainer) {
        var region = Region.of(localStackContainer.getRegion());
        var endpoint = localStackContainer.getEndpointOverride(LocalStackContainer.Service.DYNAMODB);

        return DynamoDbClient.builder()
                .credentialsProvider(awsCredentialsProvider)
                .region(region)
                .endpointOverride(endpoint)
                .build();
    }
}
