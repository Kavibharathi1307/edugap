package com.edutech.knowledgegap.controller;

import com.edutech.knowledgegap.dto.ApiResponse;
import com.edutech.knowledgegap.dto.LearningResourceResponse;
import com.edutech.knowledgegap.dto.TopicResponse;
import com.edutech.knowledgegap.entity.Student;
import com.edutech.knowledgegap.service.AuthService;
import com.edutech.knowledgegap.service.TopicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/topics")
public class TopicController {

    private final TopicService topicService;
    private final AuthService authService;

    public TopicController(TopicService topicService, AuthService authService) {
        this.topicService = topicService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TopicResponse>>> getAllTopics(
            @RequestParam(required = false) Long subjectId) {
        Student current = authService.getAuthenticatedStudent();
        List<TopicResponse> topics = topicService.getAllTopics(current, subjectId);
        return ResponseEntity.ok(ApiResponse.success("Topics retrieved successfully", topics));
    }

    @GetMapping("/{topicId}")
    public ResponseEntity<ApiResponse<TopicResponse>> getTopicById(@PathVariable Long topicId) {
        Student current = authService.getAuthenticatedStudent();
        TopicResponse topic = topicService.getTopicById(topicId, current);
        return ResponseEntity.ok(ApiResponse.success("Topic retrieved successfully", topic));
    }

    @GetMapping("/{topicId}/resources")
    public ResponseEntity<ApiResponse<List<LearningResourceResponse>>> getTopicResources(@PathVariable Long topicId) {
        List<LearningResourceResponse> resources = topicService.getTopicResources(topicId);
        return ResponseEntity.ok(ApiResponse.success("Resources retrieved successfully", resources));
    }

    @PostMapping("/{topicId}/progress")
    public ResponseEntity<ApiResponse<TopicResponse>> toggleTopicProgress(
            @PathVariable Long topicId,
            @RequestBody Map<String, Boolean> payload) {
        Student current = authService.getAuthenticatedStudent();
        boolean completed = payload.getOrDefault("completed", true);
        TopicResponse updated = topicService.markTopicProgress(topicId, current, completed);
        return ResponseEntity.ok(ApiResponse.success("Topic study progress updated", updated));
    }
}
