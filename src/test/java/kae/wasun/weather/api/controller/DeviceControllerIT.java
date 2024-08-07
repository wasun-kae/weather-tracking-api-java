package kae.wasun.weather.api.controller;

import kae.wasun.weather.api.config.AwsConfig;
import kae.wasun.weather.api.config.DynamoDBConfig;
import kae.wasun.weather.api.config.TestContainersConfig;
import kae.wasun.weather.api.helper.DynamoDBTestHelper;
import kae.wasun.weather.api.model.document.WeatherTrackingDocument;
import kae.wasun.weather.api.util.ClockUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;

import java.text.MessageFormat;
import java.time.Instant;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        void should_return_status_ok_with_data_if_device_exists() throws Exception {
            var deviceId = "mock-device-id";
            var deviceKey = MessageFormat.format("device#{0}", deviceId);
            var document = WeatherTrackingDocument.builder()
                    .PK(deviceKey)
                    .SK(deviceKey)
                    .id(deviceId)
                    .createdAt("2024-08-07T12:34:56.789Z")
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

        @Test
        void should_return_status_not_found_with_error_message_if_not_exist() throws Exception {
            var deviceId = "mock-device-id";
            var path = MessageFormat.format("/devices/{0}", deviceId);

            mockMvc.perform(get(path))
                    .andExpect(status().isNotFound())
                    .andExpect(content().json("""
                                    {
                                        "message": "Item Not Found"
                                    }
                                    """
                            )
                    );
        }
    }

    @Nested
    class create {

        @Test
        void should_return_status_created_with_data_if_device_not_exist() throws Exception {
            mockMvc.perform(post("/devices")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                        "id": "mock-device-id"
                                    }
                                    """
                            )
                    )
                    .andExpect(status().isCreated())
                    .andExpect(content().json("""
                                    {
                                        "id": "mock-device-id"
                                    }
                                    """
                            )
                    );
        }

        @Test
        void should_return_status_conflict_with_error_message_if_already_exist() throws Exception {
            var deviceId = "mock-device-id";
            var deviceKey = MessageFormat.format("device#{0}", deviceId);
            var document = WeatherTrackingDocument.builder()
                    .PK(deviceKey)
                    .SK(deviceKey)
                    .id(deviceId)
                    .createdAt("2024-08-07T12:34:56.789Z")
                    .build();

            DynamoDBTestHelper.putItem(enhancedClient, document);

            mockMvc.perform(post("/devices")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                        "id": "mock-device-id"
                                    }
                                    """
                            )
                    )
                    .andExpect(status().isConflict())
                    .andExpect(content().json("""
                                    {
                                        "message": "Item Already Exists"
                                    }
                                    """
                            )
                    );
        }

        @ParameterizedTest
        @ValueSource(strings = {
                """
                        {
                            "id": null
                        }
                        """,
                """
                        {
                            "id": ""
                        }
                        """,
                """
                        {
                            "id": " "
                        }
                        """,
        })
        void should_return_status_bad_request_with_error_message_if_request_body_is_invalid_format(String requestBody)
                throws Exception {

            mockMvc.perform(post("/devices")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(content().json("""
                                    {
                                        "message": "Invalid Format"
                                    }
                                    """
                            )
                    );
        }
    }
}
