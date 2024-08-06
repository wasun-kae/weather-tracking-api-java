package kae.wasun.weather.api.controller;

import kae.wasun.weather.api.model.dto.DeviceDto;
import kae.wasun.weather.api.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @GetMapping("/devices/{id}")
    public ResponseEntity<DeviceDto> findById(String id) {
        var deviceDto = deviceService.findById(id);
        return ResponseEntity.ok().body(deviceDto);
    }
}
