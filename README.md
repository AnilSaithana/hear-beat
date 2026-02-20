#IoT Heartbeat & Command Platform (v1)

An industry-style IoT backend platform prototype that demonstrates reliable
device lifecycle management, heartbeat-based online/offline detection, and
bi-directional command execution using MQTT and Spring Boot.

This project focuses on correctness, reliability, and platform design rather
than UI or dashboards.

⸻

##🎯 Problem Statement

In real-world IoT systems:
	•	Devices silently go offline
	•	Networks are unreliable
	•	Commands may be delivered but not executed
	•	Backends must not trust device-side claims blindly

##Most failures are operational, not functional.

This project demonstrates how a backend platform should:
	•	Detect device liveness reliably
	•	Own device state
	•	Issue commands asynchronously
	•	Confirm actual execution, not just receipt

⸻

##🧠 Key Concepts Implemented
	•	Event-driven ingestion using MQTT
	•	Backend-owned device lifecycle
	•	Heartbeat-based online/offline detection
	•	REST control plane
	•	MQTT data plane
	•	Command → execute → stateful ACK loop
	•	Reconnect-safe MQTT subscriptions

⸻

##🏗️ Architecture Overview
##ESP8266 / NodeMCU
  ├─ Publishes telemetry (heartbeat)
  ├─ Subscribes to commands
  ├─ Executes actions (LED, etc.)
  └─ Sends stateful ACKs
          |
          |  MQTT
          |
##MQTT Broker (Mosquitto)
          |
          |  MQTT
          |
##Spring Boot Backend
  ├─ MQTT Telemetry Consumer
  ├─ Device Registry (in-memory)
  ├─ Heartbeat & Offline Detector
  ├─ Command Publisher (QoS 1)
  ├─ Command ACK Consumer
  └─ REST APIs (Control Plane)
          |
          |  REST
          |
##External Clients
  ├─ curl
  └─ other backend services
##🔁 Data Flow

##Telemetry / Heartbeat

##Topic
devices/{deviceId}/telemetry
##Payload example
{
  "uptime": 323,
  "rssi": -48
}
##Each telemetry message is treated as a heartbeat.
##Command Flow
REST → MQTT → Device → MQTT ACK → Backend
##Topics
devices/{deviceId}/command
devices/{deviceId}/command/ack
Commands are:
	•	Issued via REST
	•	Delivered via MQTT
	•	Executed on device
	•	Acknowledged with actual resulting state
##📦 Tech Stack

##Backend
	•	Java 17
	•	Spring Boot
	•	Eclipse Paho MQTT Client
	•	Maven

##Device
	•	ESP8266 (ESP-12E / NodeMCU)
	•	PlatformIO
	•	PubSubClient

##Messaging
	•	Mosquitto MQTT Broker (local)

⸻

##🚀 Features

##✅ Implemented
	•	Device heartbeat ingestion
	•	Online / offline detection via scheduler
	•	Backend-owned device state
	•	REST API to query device status
	•	REST API to issue commands
	•	MQTT command publishing (QoS 1)
	•	Device command execution
	•	Stateful ACKs (actual device state)
	•	Reconnect-safe MQTT subscriptions

##❌ Not in Scope (by design)
	•	UI / dashboards
	•	Cloud deployment
	•	Authentication / ACLs
	•	Database persistence (planned)

⸻

##📡 REST APIs

Get all devices
##GET /api/devices
GET /api/devices
##Response
[
  {
    "deviceId": "node-001",
    "online": true,
    "lastSeenEpochMs": 1771499812345
  }
]
##Get single device
GET /api/devices/{deviceId}
##Send command to device
POST /api/devices/{deviceId}/commands
##Payload
{
  "type": "LED",
  "payload": "ON"
}
202 Accepted
(Command execution is asynchronous)

⚙️ How to Run Locally
##1. Start MQTT Broker
mosquitto -v -c mosquitto-local.conf
##Minimal config
listener 1883 0.0.0.0
allow_anonymous true
##2. Run Spring Boot Backend
mvn spring-boot:run
##3. Flash ESP8266 Firmware
	•	Configure Wi-Fi credentials
	•	Configure broker IP
	•	Upload using PlatformIO
	•	Open serial monitor
##4. Verify End-to-End
curl http://localhost:8080/api/devices
Send Command:
curl -X POST http://localhost:8080/api/devices/node-001/commands \
  -H "Content-Type: application/json" \
  -d '{ "type": "PING", "payload": "" }'

  🧪 Failure Scenarios Tested
	•	Device power off → offline detected
	•	Wi-Fi drop → reconnect + resubscribe
	•	Broker restart → recovery
	•	Command ACK confirms actual state

⸻

##🧩 Design Decisions
	•	Backend is the source of truth
	•	Heartbeats are time-based, not connection-based
	•	REST is control plane, MQTT is data plane
	•	ACKs confirm state, not intent
	•	In-memory store used for clarity (DB planned)

⸻

##🔮 Planned Enhancements
	•	Command lifecycle persistence (PENDING → SUCCESS → TIMEOUT)
	•	Database backing (Postgres)
	•	Retry & idempotency logic
	•	Authentication & per-device ACLs
	•	OPC UA integration as an edge data source

⸻

##👨‍💻 Author Notes

This project is intentionally built as a platform reference, not a consumer product.

It reflects how real IoT backends:
	•	Detect failures
	•	Handle unreliable devices
	•	Expose clean control planes
	•	Remain boring and reliable
