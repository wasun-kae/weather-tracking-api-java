package kae.wasun.weather.api.service;

import kae.wasun.weather.api.model.dto.DeviceDto;
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
                .build();
    }
}
