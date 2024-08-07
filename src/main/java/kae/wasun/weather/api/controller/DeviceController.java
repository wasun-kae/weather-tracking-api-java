package kae.wasun.weather.api.controller;

import kae.wasun.weather.api.model.dto.CreateDeviceDto;
import kae.wasun.weather.api.model.dto.DeviceDto;
import kae.wasun.weather.api.model.exception.ItemAlreadyExists;
import kae.wasun.weather.api.model.exception.ItemNotFoundException;
import kae.wasun.weather.api.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @GetMapping("/devices/{id}")
    public ResponseEntity<DeviceDto> findById(@PathVariable String id) throws ItemNotFoundException {
        var deviceDto = deviceService.findById(id);
        return ResponseEntity.ok().body(deviceDto);
    }

    public ResponseEntity<DeviceDto> create(CreateDeviceDto createDeviceDto) throws ItemAlreadyExists {
        var deviceDto = deviceService.create(createDeviceDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(deviceDto);
    }
}
