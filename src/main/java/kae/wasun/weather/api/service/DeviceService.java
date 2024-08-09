package kae.wasun.weather.api.service;

import kae.wasun.weather.api.model.domain.Device;
import kae.wasun.weather.api.model.dto.CreateDeviceDto;
import kae.wasun.weather.api.model.dto.DeviceDto;
import kae.wasun.weather.api.model.exception.ItemAlreadyExistsException;
import kae.wasun.weather.api.model.exception.ItemNotFoundException;
import kae.wasun.weather.api.repository.DeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeviceService {

    private final DeviceRepository deviceRepository;

    public DeviceDto findById(String id) throws ItemNotFoundException {
        var device = deviceRepository.findById(id)
                .orElseThrow(ItemNotFoundException::new);

        return DeviceDto.builder()
                .id(device.getId())
                .createdAt(device.getCreatedAt())
                .build();
    }

    public DeviceDto create(CreateDeviceDto createDeviceDto) throws ItemAlreadyExistsException {
        var deviceOptional = deviceRepository.findById(createDeviceDto.getId());

        if (deviceOptional.isPresent()) {
            throw new ItemAlreadyExistsException();
        }

        var deviceToCreate = Device.builder()
                .id(createDeviceDto.getId())
                .build();

        var createdDevice = deviceRepository.create(deviceToCreate);

        return DeviceDto.builder()
                .id(createdDevice.getId())
                .createdAt(createdDevice.getCreatedAt())
                .build();
    }
}
