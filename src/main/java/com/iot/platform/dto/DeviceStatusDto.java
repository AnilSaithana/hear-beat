package com.iot.platform.dto;


public class DeviceStatusDto {

    private String deviceId;
    private boolean online;
    private long lastSeenEpochMs;

    public DeviceStatusDto(String deviceId, boolean online, long lastSeenEpochMs) {
        this.deviceId = deviceId;
        this.online = online;
        this.lastSeenEpochMs = lastSeenEpochMs;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public boolean isOnline() {
        return online;
    }

    public long getLastSeenEpochMs() {
        return lastSeenEpochMs;
    }
}