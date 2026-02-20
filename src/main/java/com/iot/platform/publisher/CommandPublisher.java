package com.iot.platform.publisher;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.springframework.stereotype.Component;

@Component
public class CommandPublisher {

	private final MqttClient mqttClient;

	public CommandPublisher(MqttClient mqttClient) {
		this.mqttClient = mqttClient;
	}

	public void sendCommand(String deviceId, String json) throws Exception {
		String topic = "devices/" + deviceId + "/command";
		mqttClient.publish(topic, json.getBytes(), 1, false);
	}
}
