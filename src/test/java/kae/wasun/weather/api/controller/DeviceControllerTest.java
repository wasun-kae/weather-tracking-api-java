package kae.wasun.weather.api.controller;

import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DeviceControllerTest {

    private DeviceController deviceController;

    @BeforeEach
    void setUp() {
        deviceController = new DeviceController();
    }

    @Nested
    class findById {

        @Test
        void should_return_status_200_when_device_exists() {
            var deviceId = "mock-device-id";
            var actual = deviceController.findById(deviceId);

            assertThat(actual.getStatusCode().value()).isEqualTo(200);
        }
    }
}
