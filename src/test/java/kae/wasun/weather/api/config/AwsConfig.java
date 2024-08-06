package kae.wasun.weather.api.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.localstack.LocalStackContainer;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

@TestConfiguration
public class AwsConfig {

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider(LocalStackContainer localStackContainer) {
        var awsBasicCredentials = AwsBasicCredentials.create(
                localStackContainer.getAccessKey(),
                localStackContainer.getSecretKey()
        );

        return StaticCredentialsProvider.create(awsBasicCredentials);
    }
}
