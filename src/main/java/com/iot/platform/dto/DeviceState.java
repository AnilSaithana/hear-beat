package com.iot.platform.dto;

public class DeviceState {
	private final String deviceId;
	private volatile long lastSeenEpochMs;
	private volatile boolean online;

	public DeviceState(String deviceId) {
		this.deviceId = deviceId;
		this.lastSeenEpochMs = System.currentTimeMillis();
		this.online = true;
	}

	public void heartbeat() {
		this.lastSeenEpochMs = System.currentTimeMillis();
		this.online = true;
	}

	public long getLastSeenEpochMs() {
		return lastSeenEpochMs;
	}

	public boolean isOnline() {
		return online;
	}

	public void markOffline() {
		this.online = false;
	}

	public String getDeviceId() {
		return deviceId;
	}
}
