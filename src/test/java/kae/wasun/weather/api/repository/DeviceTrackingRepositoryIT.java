package kae.wasun.weather.api.repository;

import kae.wasun.weather.api.config.AwsConfig;
import kae.wasun.weather.api.config.DynamoDBConfig;
import kae.wasun.weather.api.config.TestContainersConfig;
import kae.wasun.weather.api.helper.DynamoDBTestHelper;
import kae.wasun.weather.api.model.domain.DeviceTracking;
import kae.wasun.weather.api.model.exception.ItemAlreadyExistsException;
import kae.wasun.weather.api.util.ClockUtil;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Import({AwsConfig.class, DynamoDBConfig.class, TestContainersConfig.class})
public class DeviceTrackingRepositoryIT {

    @Autowired
    private DynamoDbEnhancedClient enhancedClient;

    @Autowired
    private DeviceTrackingRepository deviceTrackingRepository;

    @MockBean
    private ClockUtil clockUtil;

    @BeforeEach
    void setUp() {
        DynamoDBTestHelper.deleteAllItems(enhancedClient);

        var mockedCurrentDateTime = Instant.parse("2024-08-07T12:34:56.789123Z");
        when(clockUtil.getCurrentTime()).thenReturn(mockedCurrentDateTime);
    }

    @Nested
    class create {

        @Test
        void should_save_new_document_if_not_exists() throws ItemAlreadyExistsException {
            var deviceId = "mock-device-id";
            var timestamp = Instant.parse("2024-08-06T00:00:00.123456Z");

            var trackingToSave = DeviceTracking.builder()
                    .timestamp(timestamp)
                    .temperature(BigDecimal.valueOf(25.1))
                    .humidity(BigDecimal.valueOf(75.3))
                    .build();

            deviceTrackingRepository.create(deviceId, trackingToSave);

            var partitionKey = MessageFormat.format("device#{0}", deviceId);
            var sortKey = MessageFormat.format("timestamp#{0}", "2024-08-06T00:00:00.123Z");
            var actual = DynamoDBTestHelper.getItem(enhancedClient, partitionKey, sortKey);

            assertThat(actual.getPK()).isEqualTo("device#mock-device-id");
            assertThat(actual.getSK()).isEqualTo("timestamp#2024-08-06T00:00:00.123Z");

            assertThat(actual.getTimestamp()).isEqualTo("2024-08-06T00:00:00.123Z");
            assertThat(actual.getCreatedAt()).isEqualTo("2024-08-07T12:34:56.789Z");
            assertThat(actual.getTemperature()).isEqualTo(BigDecimal.valueOf(25.1));
            assertThat(actual.getHumidity()).isEqualTo(BigDecimal.valueOf(75.3));
        }
    }
}
