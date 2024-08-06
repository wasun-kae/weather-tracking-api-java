package kae.wasun.weather.api.controller;

import kae.wasun.weather.api.dto.DeviceDto;
import kae.wasun.weather.api.service.DeviceService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DeviceControllerTest {

    private final String deviceId = "mock-device-id";

    @Mock
    private DeviceService deviceService;
    private DeviceController deviceController;

    @BeforeEach
    void setUp() {
        deviceController = new DeviceController(deviceService);
    }

    @Nested
    class findById {

        @BeforeEach
        void setUp() {
            var deviceDto = DeviceDto.builder()
                    .id(deviceId)
                    .build();

            when(deviceService.findById(deviceId)).thenReturn(deviceDto);
        }

        @Test
        void should_return_status_200_when_device_exists() {
            var actual = deviceController.findById(deviceId);

            assertThat(actual.getStatusCode().value()).isEqualTo(200);
        }

        @Test
        void should_return_existing_device_data() {
            var actual = deviceController.findById(deviceId);

            assertThat(actual.getBody()).isNotNull();
            assertThat(actual.getBody().getId()).isEqualTo(deviceId);
        }

        @Test
        void should_find_existing_device_by_given_id() {
            deviceController.findById(deviceId);

            verify(deviceService, times(1)).findById(deviceId);
        }
    }
}
