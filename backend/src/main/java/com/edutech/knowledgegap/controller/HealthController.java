package com.edutech.knowledgegap.controller;

import com.edutech.knowledgegap.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthController {

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, String>>> checkHealth() {
        Map<String, String> healthStatus = Map.of(
                "status", "UP",
                "application", "Intelligent Knowledge Gap Detection and Personalized Learning System",
                "version", "1.0.0"
        );
        return ResponseEntity.ok(ApiResponse.success("System is operating normally", healthStatus));
    }
}
