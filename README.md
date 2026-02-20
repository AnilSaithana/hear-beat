# 🛰️ IoT Heartbeat & Command Platform (v1)

[![Spring Boot](https://img.shields.io/badge/Backend-Spring%20Boot%203-brightgreen?style=for-the-badge&logo=springboot)](https://spring.io/)
[![MQTT](https://img.shields.io/badge/Messaging-MQTT%20(Mosquitto)-blue?style=for-the-badge&logo=mqtt)](https://mqtt.org/)
[![Hardware](https://img.shields.io/badge/Hardware-ESP8266%20%2F%20NodeMCU-orange?style=for-the-badge&logo=espressif)](https://www.espressif.com/)

An industry-style IoT backend platform prototype demonstrating reliable device lifecycle management, heartbeat-based online/offline detection, and stateful bi-directional command execution.

---

## 🎯 Problem Statement
In production IoT, networks are unreliable and devices fail silently. 
* **The Reality:** Devices go offline, commands vanish, and backends often "guess" the state.
* **The Solution:** This project implements a **Reliability-First** architecture where the backend owns the device state and confirms execution via **Stateful ACKs**.

---

## 🏗️ Architecture Overview

| Component | Role |
| :--- | :--- |
| **ESP8266 / NodeMCU** | Publishes telemetry, executes actions, and sends ACKs. |
| **Mosquitto Broker** | The high-performance MQTT Data Plane. |
| **Spring Boot Backend** | The Control Plane, Heartbeat Detector, and Device Registry. |

### 🔁 Data Flow
1. **Telemetry:** `devices/{id}/telemetry` ➡️ Used as a **Heartbeat** pulse.
2. **Command:** `REST POST` ➡️ `MQTT devices/{id}/command` ➡️ **Device Execution**.
3. **ACK:** `Device` ➡️ `MQTT devices/{id}/command/ack` ➡️ **Backend State Update**.

---

## 🚀 Features

### ✅ Implemented
- [x] **Heartbeat Ingestion:** Automated tracking of "Last Seen" timestamps.
- [x] **Offline Detection:** Scheduled task to mark inactive devices.
- [x] **Stateful ACKs:** Confirms *actual* device state, not just command receipt.
- [x] **QoS 1 Messaging:** Guaranteed delivery for critical commands.
- [x] **Reconnect-Safe:** Subscriptions automatically recover after broker restarts.

### ❌ Out of Scope (By Design)
- UI / Dashboards (Focus is on the API).
- Cloud Deployment / Auth.
- Persistence (Currently in-memory; Database planned).

---

## 📡 REST API (Control Plane)

### 1. List All Devices
`GET /api/devices`
```json
[
  {
    "deviceId": "node-001",
    "online": true,
    "lastSeenEpochMs": 1771499812345
  }
]
```
### 🔁 2. Data Flow & Communication Strategy**

### Telemetry / Heartbeat
* **Topic:** `devices/{deviceId}/telemetry`
* **Payload:** `{ "uptime": 323, "rssi": -48 }`
* **Logic:** Each incoming telemetry message is treated as a physical heartbeat. If no message is received within the timeout window, the backend marks the device **OFFLINE**.

### Command Execution Loop
The platform uses a stateful loop to ensure commands are actually executed, not just sent:
1. **REST Call:** `POST /api/devices/{id}/commands`
2. **MQTT Issue:** Backend publishes to `devices/{deviceId}/command` (QoS 1).
3. **Device Action:** ESP8266 receives, toggles hardware (e.g., LED), and confirms.
4. **Stateful ACK:** Device publishes result to `devices/{deviceId}/command/ack`.
5. **Update:** Backend updates the registry with the *actual* resulting state.

---

## 📦 3. Tech Stack

| Layer | Technology |
| :--- | :--- |
| **Backend** | Java 17, Spring Boot, Eclipse Paho MQTT Client, Maven |
| **Device** | ESP8266 (NodeMCU), PlatformIO, PubSubClient |
| **Messaging** | Mosquitto MQTT Broker (Local/Docker) |

---

## 🚀 4. Features & Roadmap

### ✅ Implemented
- [x] **Heartbeat Ingestion:** Automated tracking of "Last Seen" timestamps.
- [x] **Online/Offline Detection:** Scheduler-based presence monitoring.
- [x] **Stateful ACKs:** Verification of actual device state.
- [x] **Reconnect-Safe:** MQTT subscriptions survive broker/network restarts.
- [x] **REST Control Plane:** Clean API for external service integration.

### ❌ Not in Scope (By Design)
- UI / Dashboards (Focus is on the API layer).
- Authentication / ACLs (Protocol-focused prototype).
- Database Persistence (Planned for next version).

---

## 📡 5. REST API Reference

### List All Devices
`GET /api/devices`
```json
[
  {
    "deviceId": "node-001",
    "online": true,
    "lastSeenEpochMs": 1771499812345
  }
]
```
### Send Command to Device
`POST /api/devices/{deviceId}/commands`

**Payload:**
```json
{ 
  "type": "LED", 
  "payload": "ON" 
}
```
## ⚙️ 6. How to Run Locally

### 1. Start MQTT Broker
mosquitto -v -c mosquitto-local.conf

# Minimal mosquitto-local.conf content:
# listener 1883 0.0.0.0
# allow_anonymous true

### 2. Run Spring Boot Backend
mvn spring-boot:run

### 3. Flash ESP8266 Firmware
# Configure Wi-Fi credentials and broker IP in main.cpp
# Use PlatformIO to Build and Upload

### 4. Verify End-to-End
# Check registry
curl http://localhost:8080/api/devices

# Send Command (Example: PING)
curl -X POST http://localhost:8080/api/devices/node-001/commands \
-H "Content-Type: application/json" \
-d '{ "type": "PING", "payload": "" }'

---

## 🧩 7. Design Decisions
* Heartbeats vs LWT: Time-based pulses detect hung devices better than TCP flags.
* Planes: REST for Control Plane; MQTT for Data Plane.
* Reliability: Uses MQTT QoS 1 for guaranteed command delivery.

---

## 🔮 8. Planned Enhancements
- [ ] Command lifecycle persistence (PENDING -> SUCCESS -> TIMEOUT).
- [ ] PostgreSQL integration for device history.
- [ ] Authentication & per-device ACLs.

---

## 👨‍💻 Author Notes
This project reflects how industrial IoT backends handle unreliable hardware while maintaining a clean, predictable API.
