package kae.wasun.weather.api.controller;

import kae.wasun.weather.api.config.AwsConfig;
import kae.wasun.weather.api.config.DynamoDBConfig;
import kae.wasun.weather.api.config.TestContainersConfig;
import kae.wasun.weather.api.helper.DynamoDBTestHelper;
import kae.wasun.weather.api.model.document.WeatherTrackingDocument;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

import java.text.MessageFormat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Import({AwsConfig.class, DynamoDBConfig.class, TestContainersConfig.class})
public class DeviceControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private DynamoDbEnhancedClient enhancedClient;

    @BeforeEach
    void setUp() {
        DynamoDBTestHelper.deleteAllItems(enhancedClient);
    }

    @Nested
    class findById {

        @Test
        void should_return_status_ok_with_device_data_if_exists() throws Exception {
            var deviceId = "mock-device-id";
            var deviceKey = MessageFormat.format("device#{0}", deviceId);
            var document = WeatherTrackingDocument.builder()
                    .PK(deviceKey)
                    .SK(deviceKey)
                    .id(deviceId)
                    .build();

            DynamoDBTestHelper.putItem(enhancedClient, document);
            var path = MessageFormat.format("/devices/{0}", deviceId);

            mockMvc.perform(get(path))
                    .andExpect(status().isOk())
                    .andExpect(content().json("""
                                    {
                                        "id": "mock-device-id"
                                    }
                                    """
                            )
                    );
        }
    }
}
