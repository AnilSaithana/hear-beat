package com.iot.platform.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iot.platform.dto.CommandRequest;
import com.iot.platform.publisher.CommandPublisher;

import tools.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/devices")
public class CommandController {

    private final CommandPublisher publisher;
    private final ObjectMapper mapper = new ObjectMapper();

    public CommandController(CommandPublisher publisher) {
        this.publisher = publisher;
    }

    @PostMapping("/{deviceId}/commands")
    public ResponseEntity<?> sendCommand(
            @PathVariable String deviceId,
            @RequestBody CommandRequest request) throws Exception {

        String json = mapper.writeValueAsString(request);
        publisher.sendCommand(deviceId, json);
        return ResponseEntity.accepted().build();
    }
    
    @GetMapping("/{deviceId}/commands/led/on")
    public ResponseEntity<?> ledOn(@PathVariable String deviceId) throws Exception {

        CommandRequest request = new CommandRequest();
        request.setType("LED");
        request.setPayload(deviceId.toUpperCase());

        publisher.sendCommand("node-001", mapper.writeValueAsString(request));
        return ResponseEntity.accepted().build();
    }
}
