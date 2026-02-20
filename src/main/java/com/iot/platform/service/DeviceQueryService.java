package com.iot.platform.service;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.iot.platform.config.DeviceRegistry;
import com.iot.platform.dto.DeviceStatusDto;

@Service
public class DeviceQueryService {

    private final DeviceRegistry registry;

    public DeviceQueryService(DeviceRegistry registry) {
        this.registry = registry;
    }

    public List<DeviceStatusDto> getAllDevices() {
        return registry.allDevices().values().stream()
                .map(d -> new DeviceStatusDto(
                        d.getDeviceId(),
                        d.isOnline(),
                        d.getLastSeenEpochMs()
                ))
                .collect(Collectors.toList());
    }

    public DeviceStatusDto getDevice(String deviceId) {
        var device = registry.allDevices().get(deviceId);
        if (device == null) {
            return null;
        }
        return new DeviceStatusDto(
                device.getDeviceId(),
                device.isOnline(),
                device.getLastSeenEpochMs()
        );
    }
}
