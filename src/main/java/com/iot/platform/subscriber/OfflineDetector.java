package com.iot.platform.subscriber;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.iot.platform.config.DeviceRegistry;

@Component
public class OfflineDetector {

	private static final long OFFLINE_THRESHOLD_MS = 15_000;

	private final DeviceRegistry registry;

	public OfflineDetector(DeviceRegistry registry) {
		this.registry = registry;
	}

	@Scheduled(fixedDelay = 5000)
	public void detectOfflineDevices() {
		long now = System.currentTimeMillis();

		registry.allDevices().values().forEach(device -> {
			if (device.isOnline() && (now - device.getLastSeenEpochMs()) > OFFLINE_THRESHOLD_MS) {

				device.markOffline();
				System.out.println("❌ DEVICE OFFLINE: " + device.getDeviceId());
			}
		});
	}
}
