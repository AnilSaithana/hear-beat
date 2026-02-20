package com.iot.platform.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.iot.platform.dto.DeviceState;

@Component
public class DeviceRegistry {

	private final Map<String, DeviceState> devices = new ConcurrentHashMap<>();

	public DeviceState heartbeat(String deviceId) {
		return devices.compute(deviceId, (id, state) -> {
			if (state == null) {
				return new DeviceState(id);
			}
			state.heartbeat();
			return state;
		});
	}

	public Map<String, DeviceState> allDevices() {
		return devices;
	}
}
