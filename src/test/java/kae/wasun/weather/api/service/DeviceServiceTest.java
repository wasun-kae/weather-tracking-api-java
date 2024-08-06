package kae.wasun.weather.api.service;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class DeviceServiceTest {

    private DeviceService deviceService;

    @BeforeEach
    void setUp() {
        deviceService = new DeviceService();
    }

    @Nested
    class findById {

        @Test
        void should_return_existing_device() {
            var deviceId = "mock-device-id";
            var actual = deviceService.findById(deviceId);

            assertThat(actual.getId()).isEqualTo(deviceId);
        }
    }
}
