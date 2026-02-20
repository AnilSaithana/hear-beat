package com.iot.platform.subscriber;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.stereotype.Component;

import com.iot.platform.config.DeviceRegistry;

import jakarta.annotation.PostConstruct;

@Component
public class TelemetrySubscriber {

	private final MqttClient client;
	private final DeviceRegistry registry;

	public TelemetrySubscriber(MqttClient client, DeviceRegistry registry) {
		this.client = client;
		this.registry = registry;
	}

	@PostConstruct
	public void subscribe() throws Exception {
		client.subscribe("devices/+/telemetry", (topic, message) -> {
			String deviceId = topic.split("/")[1];
			registry.heartbeat(deviceId);

			System.out.println(message + " => Heartbeat from " + deviceId + " at " + System.currentTimeMillis());
		});
	}
}
