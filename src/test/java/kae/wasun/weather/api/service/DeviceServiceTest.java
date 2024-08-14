package kae.wasun.weather.api.service;

import kae.wasun.weather.api.model.domain.Device;
import kae.wasun.weather.api.model.dto.CreateDeviceDto;
import kae.wasun.weather.api.model.exception.ItemAlreadyExistsException;
import kae.wasun.weather.api.model.exception.ItemNotFoundException;
import kae.wasun.weather.api.repository.DeviceRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;
    private DeviceService deviceService;

    @BeforeEach
    void setUp() {
        deviceService = new DeviceService(deviceRepository);
    }

    @Nested
    class findById {

        @BeforeEach
        void setUp() {
            var deviceId = "mock-device-id";
            var createdAt = Instant.parse("2024-08-07T12:34:56.789Z");
            var device = Device.builder()
                    .id(deviceId)
                    .createdAt(createdAt)
                    .build();

            when(deviceRepository.findById(deviceId)).thenReturn(Optional.of(device));
        }

        @Test
        void should_return_device_if_exists() throws ItemNotFoundException {
            var deviceId = "mock-device-id";
            var actual = deviceService.findById(deviceId);

            assertThat(actual.getId()).isEqualTo("mock-device-id");
            assertThat(actual.getCreatedAt()).isEqualTo(Instant.parse("2024-08-07T12:34:56.789Z"));
        }

        @Test
        void should_throw_item_not_found_error_if_not_exists() {
            var deviceId = "mock-device-id";
            when(deviceRepository.findById(deviceId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> deviceService.findById(deviceId))
                    .isInstanceOf(ItemNotFoundException.class)
                    .hasMessage("Item Not Found");
        }

        @Test
        void should_find_device_with_given_id_from_the_database() throws ItemNotFoundException {
            var deviceId = "mock-device-id";
            deviceService.findById(deviceId);

            verify(deviceRepository, times(1)).findById(deviceId);
        }
    }

    @Nested
    class create {

        @BeforeEach
        void setUp() throws ItemAlreadyExistsException {
            var deviceId = "mock-device-id";
            var createdAt = Instant.parse("2024-08-07T12:34:56.789Z");
            var deviceToCreate = Device.builder()
                    .id(deviceId)
                    .build();

            var createdDevice = Device.builder()
                    .id(deviceId)
                    .createdAt(createdAt)
                    .build();

            when(deviceRepository.create(deviceToCreate)).thenReturn(createdDevice);
        }

        @Test
        void should_throw_item_already_exists_error_if_device_already_exists() throws ItemAlreadyExistsException {
            var deviceId = "mock-device-id";
            var deviceToCreate = Device.builder()
                    .id(deviceId)
                    .build();

            when(deviceRepository.create(deviceToCreate)).thenThrow(new ItemAlreadyExistsException());

            var createDeviceDto = CreateDeviceDto.builder().id(deviceId).build();

            assertThatThrownBy(() ->
                    deviceService.create(createDeviceDto)
            )
                    .isInstanceOf(ItemAlreadyExistsException.class)
                    .hasMessage("Item Already Exists");
        }

        @Test
        void should_return_a_created_device() throws ItemAlreadyExistsException {
            var deviceId = "mock-device-id";
            var createDeviceDto = CreateDeviceDto.builder().id(deviceId).build();
            var actual = deviceService.create(createDeviceDto);

            assertThat(actual.getId()).isEqualTo("mock-device-id");
            assertThat(actual.getCreatedAt()).isEqualTo(Instant.parse("2024-08-07T12:34:56.789Z"));
        }

        @Test
        void should_save_a_new_device_to_the_database() throws ItemAlreadyExistsException {
            var deviceId = "mock-device-id";
            var createDeviceDto = CreateDeviceDto.builder().id(deviceId).build();
            deviceService.create(createDeviceDto);

            var device = Device.builder().id(deviceId).build();
            verify(deviceRepository, times(1)).create(device);
        }
    }
}
