package com.edutech.knowledgegap.controller;

import com.edutech.knowledgegap.dto.ApiResponse;
import com.edutech.knowledgegap.dto.DashboardStatsResponse;
import com.edutech.knowledgegap.dto.ProgressHistoryResponse;
import com.edutech.knowledgegap.dto.TopicPerformanceResponse;
import com.edutech.knowledgegap.entity.Student;
import com.edutech.knowledgegap.service.AnalyticsService;
import com.edutech.knowledgegap.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;
    private final AuthService authService;

    public AnalyticsController(AnalyticsService analyticsService, AuthService authService) {
        this.analyticsService = analyticsService;
        this.authService = authService;
    }

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> getDashboardStats() {
        Student current = authService.getAuthenticatedStudent();
        DashboardStatsResponse stats = analyticsService.getDashboardStats(current);
        return ResponseEntity.ok(ApiResponse.success("Dashboard statistics loaded", stats));
    }

    @GetMapping("/topics")
    public ResponseEntity<ApiResponse<List<TopicPerformanceResponse>>> getTopicOverview() {
        Student current = authService.getAuthenticatedStudent();
        List<TopicPerformanceResponse> topics = analyticsService.getTopicMasteryOverview(current);
        return ResponseEntity.ok(ApiResponse.success("Topic mastery overview loaded", topics));
    }

    @GetMapping("/progress")
    public ResponseEntity<ApiResponse<ProgressHistoryResponse>> getProgressHistory() {
        Student current = authService.getAuthenticatedStudent();
        ProgressHistoryResponse progress = analyticsService.getProgressHistory(current);
        return ResponseEntity.ok(ApiResponse.success("Progress trajectory loaded", progress));
    }
}
