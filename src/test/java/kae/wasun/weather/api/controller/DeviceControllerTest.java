package kae.wasun.weather.api.controller;

import kae.wasun.weather.api.model.dto.CreateDeviceDto;
import kae.wasun.weather.api.model.dto.DeviceDto;
import kae.wasun.weather.api.model.exception.ItemAlreadyExists;
import kae.wasun.weather.api.model.exception.ItemNotFoundException;
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
        void setUp() throws ItemNotFoundException {
            var deviceDto = DeviceDto.builder()
                    .id(deviceId)
                    .build();

            when(deviceService.findById(deviceId)).thenReturn(deviceDto);
        }

        @Test
        void should_return_status_200_when_device_exists() throws ItemNotFoundException {
            var actual = deviceController.findById(deviceId);

            assertThat(actual.getStatusCode().value()).isEqualTo(200);
        }

        @Test
        void should_return_existing_device_data() throws ItemNotFoundException {
            var actual = deviceController.findById(deviceId);

            assertThat(actual.getBody()).isNotNull();
            assertThat(actual.getBody().getId()).isEqualTo(deviceId);
        }

        @Test
        void should_find_existing_device_by_given_id() throws ItemNotFoundException {
            deviceController.findById(deviceId);

            verify(deviceService, times(1)).findById(deviceId);
        }
    }

    @Nested
    class create {

        @BeforeEach
        void setUp() throws ItemAlreadyExists {
            var createDeviceDto = CreateDeviceDto.builder().id(deviceId).build();
            var deviceDto = DeviceDto.builder()
                    .id(deviceId)
                    .build();

            when(deviceService.create(createDeviceDto)).thenReturn(deviceDto);
        }

        @Test
        void should_return_status_201_when_a_new_device_created() throws ItemAlreadyExists {
            var createDeviceDto = CreateDeviceDto.builder().id(deviceId).build();
            var actual = deviceController.create(createDeviceDto);

            assertThat(actual.getStatusCode().value()).isEqualTo(201);
        }

        @Test
        void should_return_created_device() throws ItemAlreadyExists {
            var createDeviceDto = CreateDeviceDto.builder().id(deviceId).build();
            var actual = deviceController.create(createDeviceDto);

            assertThat(actual.getBody()).isNotNull();
            assertThat(actual.getBody().getId()).isEqualTo(deviceId);
        }

        @Test
        void should_create_given_device() throws ItemAlreadyExists {
            var createDeviceDto = CreateDeviceDto.builder().id(deviceId).build();
            deviceController.create(createDeviceDto);

            verify(deviceService, times(1)).create(createDeviceDto);
        }
    }
}
