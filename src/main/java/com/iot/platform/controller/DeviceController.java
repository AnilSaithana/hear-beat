package com.iot.platform.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iot.platform.dto.DeviceStatusDto;
import com.iot.platform.service.DeviceQueryService;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    private final DeviceQueryService service;

    public DeviceController(DeviceQueryService service) {
        this.service = service;
    }

    @GetMapping
    public List<DeviceStatusDto> getAllDevices() {
        return service.getAllDevices();
    }

    @GetMapping("/{deviceId}")
    public ResponseEntity<DeviceStatusDto> getDevice(@PathVariable String deviceId) {
        DeviceStatusDto device = service.getDevice(deviceId);
        if (device == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(device);
    }
}
