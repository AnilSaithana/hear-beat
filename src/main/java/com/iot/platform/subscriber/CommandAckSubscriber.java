package com.iot.platform.subscriber;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class CommandAckSubscriber {

	private final MqttClient client;

	public CommandAckSubscriber(MqttClient client) {
		this.client = client;
	}

	@PostConstruct
	public void subscribe() throws Exception {
		client.subscribe("devices/+/command/ack", (topic, message) -> {
			String deviceId = topic.split("/")[1];
			String payload = new String(message.getPayload());
			System.out.println("ACK from " + deviceId + ": " + payload);
		});
	}
}
