package kae.wasun.weather.api.service;

import kae.wasun.weather.api.dto.DeviceDto;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {

    public DeviceDto findById(String id) {
        return DeviceDto.builder()
                .id(id)
                .build();
    }
}
