package kae.wasun.weather.api.repository;

import kae.wasun.weather.api.config.AwsConfig;
import kae.wasun.weather.api.config.DynamoDBConfig;
import kae.wasun.weather.api.config.TestContainersConfig;
import kae.wasun.weather.api.helper.DynamoDBTestHelper;
import kae.wasun.weather.api.model.document.WeatherTrackingDocument;
import kae.wasun.weather.api.model.domain.Device;
import kae.wasun.weather.api.model.exception.ItemAlreadyExists;
import kae.wasun.weather.api.util.ClockUtil;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

import java.text.MessageFormat;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Import({AwsConfig.class, DynamoDBConfig.class, TestContainersConfig.class})
public class DeviceRepositoryIT {

    @Autowired
    private DynamoDbEnhancedClient enhancedClient;

    @Autowired
    private DeviceRepository deviceRepository;

    @MockBean
    private ClockUtil clockUtil;

    @BeforeEach
    void setUp() {
        DynamoDBTestHelper.deleteAllItems(enhancedClient);

        var mockedCurrentDateTime = Instant.parse("2024-08-07T12:34:56.789000Z");
        when(clockUtil.getCurrentTime()).thenReturn(mockedCurrentDateTime);
    }

    @Nested
    class findById {

        @Test
        void should_return_device_if_exists() {
            var deviceId = "mock-device-id";
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
            assertThat(actual.get().getId()).isEqualTo("mock-device-id");
        }

        @Test
        void should_return_empty_if_not_exist() {
            var deviceId = "mock-device-id";
            var actual = deviceRepository.findById(deviceId);

            assertThat(actual.isEmpty()).isTrue();
        }
    }

    @Nested
    class create {

        @Test
        void should_return_created_device_if_not_exists() throws ItemAlreadyExists {
            var deviceId = "mock-device-id";
            var deviceToSave = Device.builder()
                    .id(deviceId)
                    .build();

            var actual = deviceRepository.create(deviceToSave);

            assertThat(actual.getId()).isEqualTo("mock-device-id");
        }

        @Test
        void should_save_new_document_if_not_exists() throws ItemAlreadyExists {
            var deviceId = "mock-device-id";
            var deviceToSave = Device.builder()
                    .id(deviceId)
                    .build();

            deviceRepository.create(deviceToSave);

            var partitionKey = MessageFormat.format("device#{0}", deviceId);
            var sortKey = MessageFormat.format("device#{0}", deviceId);
            var savedDocument = DynamoDBTestHelper.getItem(enhancedClient, partitionKey, sortKey);

            var mockedCurrentDateTime = Instant.parse("2024-08-07T12:34:56.789Z").toString();

            assertThat(savedDocument.getPK()).isEqualTo("device#mock-device-id");
            assertThat(savedDocument.getSK()).isEqualTo("device#mock-device-id");
            assertThat(savedDocument.getId()).isEqualTo("mock-device-id");
            assertThat(savedDocument.getCreatedAt()).isEqualTo(mockedCurrentDateTime);
        }

        @Test
        void should_throw_item_already_exists_error_if_device_already_exists() {
            var deviceId = "mock-device-id";
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
                    deviceRepository.create(device)
            )
                    .isInstanceOf(ItemAlreadyExists.class)
                    .hasMessage("Item Already Exists");
        }
    }
}
