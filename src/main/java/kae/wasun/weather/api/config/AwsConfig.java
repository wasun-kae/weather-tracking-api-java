package kae.wasun.weather.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

@Configuration
public class AwsConfig {

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider() {
        var awsBasicCredentials = AwsBasicCredentials.create(
                "test",
                "test"
        );

        return StaticCredentialsProvider.create(awsBasicCredentials);
    }
}
