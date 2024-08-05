package kae.wasun.weather.api.service;

import kae.wasun.weather.api.dto.DeviceDto;

public class DeviceService {

    public DeviceDto findById(String id) {
        return DeviceDto.builder()
                .id(id)
                .build();
    }
}
