package kae.wasun.weather.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeviceController {

    @GetMapping("/devices/{id}")
    public ResponseEntity<?> findById(String id) {
        return ResponseEntity.ok().build();
    }
}
