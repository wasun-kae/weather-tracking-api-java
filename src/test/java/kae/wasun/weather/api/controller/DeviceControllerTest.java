package kae.wasun.weather.api.controller;

import kae.wasun.weather.api.model.dto.CreateDeviceDto;
import kae.wasun.weather.api.model.dto.DeviceDto;
import kae.wasun.weather.api.model.exception.ItemAlreadyExistsException;
import kae.wasun.weather.api.model.exception.ItemNotFoundException;
import kae.wasun.weather.api.service.DeviceService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class DeviceControllerTest {

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
            var deviceId = "mock-device-id";
            var createdAt = Instant.parse("2024-08-07T12:34:56.789000Z");
            var deviceDto = DeviceDto.builder()
                    .id(deviceId)
                    .createdAt(createdAt)
                    .build();

            when(deviceService.findById(deviceId)).thenReturn(deviceDto);
        }

        @Test
        void should_return_status_200_when_device_exists() throws ItemNotFoundException {
            var deviceId = "mock-device-id";
            var actual = deviceController.findById(deviceId);

            assertThat(actual.getStatusCode().value()).isEqualTo(200);
        }

        @Test
        void should_return_existing_device_data() throws ItemNotFoundException {
            var deviceId = "mock-device-id";
            var actual = deviceController.findById(deviceId);

            assertThat(actual.getBody()).isNotNull();
            assertThat(actual.getBody().getId()).isEqualTo("mock-device-id");
            assertThat(actual.getBody().getCreatedAt()).isEqualTo(Instant.parse("2024-08-07T12:34:56.789000Z"));
        }

        @Test
        void should_find_existing_device_by_given_id() throws ItemNotFoundException {
            var deviceId = "mock-device-id";
            deviceController.findById(deviceId);

            verify(deviceService, times(1)).findById(deviceId);
        }
    }

    @Nested
    class create {

        @BeforeEach
        void setUp() throws ItemAlreadyExistsException {
            var deviceId = "mock-device-id";
            var createdAt = Instant.parse("2024-08-07T12:34:56.789000Z");
            var createDeviceDto = CreateDeviceDto
                    .builder()
                    .id(deviceId)
                    .build();

            var deviceDto = DeviceDto.builder()
                    .id(deviceId)
                    .createdAt(createdAt)
                    .build();

            when(deviceService.create(createDeviceDto)).thenReturn(deviceDto);
        }

        @Test
        void should_return_status_201_when_a_new_device_created() throws ItemAlreadyExistsException {
            var deviceId = "mock-device-id";
            var createDeviceDto = CreateDeviceDto.builder().id(deviceId).build();

            var actual = deviceController.create(createDeviceDto);

            assertThat(actual.getStatusCode().value()).isEqualTo(201);
        }

        @Test
        void should_return_created_device() throws ItemAlreadyExistsException {
            var deviceId = "mock-device-id";
            var createDeviceDto = CreateDeviceDto.builder().id(deviceId).build();

            var actual = deviceController.create(createDeviceDto);

            assertThat(actual.getBody()).isNotNull();
            assertThat(actual.getBody().getId()).isEqualTo("mock-device-id");
            assertThat(actual.getBody().getCreatedAt()).isEqualTo(Instant.parse("2024-08-07T12:34:56.789000Z"));
        }

        @Test
        void should_create_given_device() throws ItemAlreadyExistsException {
            var deviceId = "mock-device-id";
            var createDeviceDto = CreateDeviceDto.builder().id(deviceId).build();

            deviceController.create(createDeviceDto);

            verify(deviceService, times(1)).create(createDeviceDto);
        }
    }
}
