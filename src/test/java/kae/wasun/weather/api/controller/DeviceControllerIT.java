package kae.wasun.weather.api.controller;

import kae.wasun.weather.api.config.AwsConfig;
import kae.wasun.weather.api.config.DynamoDBConfig;
import kae.wasun.weather.api.config.TestContainersConfig;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.text.MessageFormat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Import({AwsConfig.class, TestContainersConfig.class, DynamoDBConfig.class})
public class DeviceControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    class findById {

        @Test
        void should_return_status_ok() throws Exception {
            var deviceId = "mock-device-id";
            var path = MessageFormat.format("/devices/{0}", deviceId);

            mockMvc.perform(get(path))
                    .andExpect(status().isOk());
        }
    }
}
