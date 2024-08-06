package kae.wasun.weather.api.repository;

import kae.wasun.weather.api.config.AwsConfig;
import kae.wasun.weather.api.config.DynamoDBConfig;
import kae.wasun.weather.api.config.TestContainersConfig;
import kae.wasun.weather.api.helper.DynamoDBTestHelper;
import kae.wasun.weather.api.model.document.WeatherTrackingDocument;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

import java.text.MessageFormat;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Import({AwsConfig.class, DynamoDBConfig.class, TestContainersConfig.class})
public class DeviceRepositoryIT {

    @Autowired
    private DynamoDbEnhancedClient enhancedClient;

    @Autowired
    private DeviceRepository deviceRepository;

    @BeforeEach
    void setUp() {
        DynamoDBTestHelper.deleteAllItems(enhancedClient);
    }

    @Nested
    class findById {

        @Test
        void should_return_device_if_exists() {
            var deviceId = "mock-device-id";
            var deviceKey = MessageFormat.format("device#{0}", deviceId);
            var document = WeatherTrackingDocument.builder()
                    .PK(deviceKey)
                    .SK(deviceKey)
                    .id(deviceId)
                    .build();

            DynamoDBTestHelper.putItem(enhancedClient, document);
            var actual = deviceRepository.findById(deviceId);

            assertThat(actual.isPresent()).isTrue();
            assertThat(actual.get().getId()).isEqualTo(deviceId);
        }

        @Test
        void should_return_empty_if_not_exist() {
            var deviceId = "mock-device-id";
            var actual = deviceRepository.findById(deviceId);

            assertThat(actual.isEmpty()).isTrue();
        }
    }
}
