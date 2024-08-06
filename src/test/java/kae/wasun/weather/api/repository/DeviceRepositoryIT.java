package kae.wasun.weather.api.repository;

import kae.wasun.weather.api.config.AwsConfig;
import kae.wasun.weather.api.config.DynamoDBConfig;
import kae.wasun.weather.api.config.TestContainersConfig;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Import({AwsConfig.class, TestContainersConfig.class, DynamoDBConfig.class})
public class DeviceRepositoryIT {

    @Nested
    class findById {

        @Test
        void should_return_device_document_by_id() {
            var deviceId = "mock-device-id";
        }
    }
}
