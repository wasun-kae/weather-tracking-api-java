package kae.wasun.weather.api.repository;

import kae.wasun.weather.api.config.AwsConfig;
import kae.wasun.weather.api.config.DynamoDBConfig;
import kae.wasun.weather.api.config.TestContainersConfig;
import kae.wasun.weather.api.helper.DynamoDBTestHelper;
import kae.wasun.weather.api.model.document.WeatherTrackingDocument;
import kae.wasun.weather.api.model.domain.Device;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

import java.text.MessageFormat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Import({AwsConfig.class, DynamoDBConfig.class, TestContainersConfig.class})
public class DeviceRepositoryIT {

    private final String deviceId = "mock-device-id";

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
            var partitionKey = MessageFormat.format("device#{0}", deviceId);
            var sortKey = MessageFormat.format("device#{0}", deviceId);
            var document = WeatherTrackingDocument.builder()
                    .PK(partitionKey)
                    .SK(sortKey)
                    .id(deviceId)
                    .build();

            DynamoDBTestHelper.putItem(enhancedClient, document);
            var actual = deviceRepository.findById(deviceId);

            assertThat(actual.isPresent()).isTrue();
            assertThat(actual.get().getId()).isEqualTo(deviceId);
        }

        @Test
        void should_return_empty_if_not_exist() {
            var actual = deviceRepository.findById(deviceId);

            assertThat(actual.isEmpty()).isTrue();
        }
    }

    @Nested
    class save {

        @Test
        void should_save_new_device_if_not_exists() {
            var deviceToSave = Device.builder()
                    .id(deviceId)
                    .build();

            var actual = deviceRepository.save(deviceToSave);

            assertThat(actual.getId()).isEqualTo(deviceId);
        }

        @Test
        void should_throw_an_error_if_device_already_exists() {
            var device = Device.builder()
                    .id(deviceId)
                    .build();

            var partitionKey = MessageFormat.format("device#{0}", deviceId);
            var sortKey = MessageFormat.format("device#{0}", deviceId);
            var existingDocument = WeatherTrackingDocument.builder()
                    .PK(partitionKey)
                    .SK(sortKey)
                    .id(deviceId)
                    .build();

            DynamoDBTestHelper.putItem(enhancedClient, existingDocument);

            assertThatThrownBy(() ->
                    deviceRepository.save(device)
            ).isInstanceOf(ConditionalCheckFailedException.class);
        }
    }
}
