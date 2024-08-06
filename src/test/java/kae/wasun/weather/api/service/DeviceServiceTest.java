package kae.wasun.weather.api.service;

import kae.wasun.weather.api.model.domain.Device;
import kae.wasun.weather.api.model.exception.ItemNotFoundException;
import kae.wasun.weather.api.repository.DeviceRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class DeviceServiceTest {

    private final String deviceId = "mock-device-id";

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
            var device = Device.builder().id(deviceId).build();
            when(deviceRepository.findById(deviceId)).thenReturn(Optional.of(device));
        }

        @Test
        void should_return_device_if_exists() throws ItemNotFoundException {
            var actual = deviceService.findById(deviceId);

            assertThat(actual.getId()).isEqualTo(deviceId);
        }

        @Test
        void should_throw_item_not_found_error_if_not_exists() {
            when(deviceRepository.findById(deviceId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> deviceService.findById(deviceId))
                    .isInstanceOf(ItemNotFoundException.class)
                    .hasMessage("Item Not Found");
        }

        @Test
        void should_find_device_from_given_id() throws ItemNotFoundException {
            deviceService.findById(deviceId);

            verify(deviceRepository, times(1)).findById(deviceId);
        }
    }
}
