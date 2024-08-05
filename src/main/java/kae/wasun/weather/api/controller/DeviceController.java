package kae.wasun.weather.api.controller;

import org.springframework.http.ResponseEntity;

public class DeviceController {

    public ResponseEntity<?> findById(String id) {
        return ResponseEntity.ok().build();
    }
}
