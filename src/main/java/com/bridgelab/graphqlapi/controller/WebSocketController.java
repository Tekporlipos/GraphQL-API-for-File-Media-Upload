package com.bridgelab.graphqlapi.controller;

import com.bridgelab.graphqlapi.model.FileMetadata;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class WebSocketController {

    @MessageMapping("/notify")
    @SendTo("/topic/{sessionId}")
    public Map<String, Object> notifyFileUpload(@Header("simpSessionId") String sessionId, FileMetadata fileMetadata) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "File upload successful!");
        response.put("data", fileMetadata);
        response.put("sessionId", sessionId);
        return response;
    }
}

