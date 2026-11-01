package com.edutech.knowledgegap.service;

import com.edutech.knowledgegap.dto.DashboardStatsResponse;
import com.edutech.knowledgegap.dto.ProgressHistoryResponse;
import com.edutech.knowledgegap.dto.TopicPerformanceResponse;
import com.edutech.knowledgegap.entity.Student;

import java.util.List;

public interface AnalyticsService {

    /**
     * Aggregates student dashboard metrics including scores, strongest/weakest topics,
     * and completion counters.
     */
    DashboardStatsResponse getDashboardStats(Student student);

    /**
     * Retrieves the latest performance status for all topics (for Radar Chart).
     */
    List<TopicPerformanceResponse> getTopicMasteryOverview(Student student);

    /**
     * Retrieves historical attempt progression for rendering Chart.js Line Curves.
     */
    ProgressHistoryResponse getProgressHistory(Student student);
}
