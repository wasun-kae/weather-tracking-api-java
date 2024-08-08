package kae.wasun.weather.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DynamoDBConfig {

    @Bean
    public DynamoDbEnhancedClient dynamoDbEnhancedClient(AwsCredentialsProvider awsCredentialsProvider)
            throws URISyntaxException {

        var dynamoDbClient = DynamoDbClient.builder()
                .credentialsProvider(awsCredentialsProvider)
                .region(Region.of("ap-southeast-1"))
                .endpointOverride(new URI("http://localhost:4566"))
                .build();

        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(dynamoDbClient)
                .build();
    }
}
