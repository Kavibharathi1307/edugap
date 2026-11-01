package com.edutech.knowledgegap.service;

import com.edutech.knowledgegap.dto.TopicPerformanceResponse;
import com.edutech.knowledgegap.entity.QuizAttempt;
import com.edutech.knowledgegap.entity.Student;
import com.edutech.knowledgegap.entity.TopicPerformance;

import java.util.List;
import java.util.Map;

public interface KnowledgeGapDetectionService {

    /**
     * Evaluates submitted question answers by topic, computes accuracy percentages,
     * assigns deterministic gap categories, handles unassessed topics,
     * and persists performance metrics to PostgreSQL.
     */
    List<TopicPerformance> analyzeAndPersistGaps(
            QuizAttempt attempt,
            Student student,
            Map<Long, Integer> topicTotalQuestions,
            Map<Long, Integer> topicCorrectAnswers
    );

    /**
     * Formats TopicPerformance entities into API responses with human-friendly labels and UI colors.
     */
    List<TopicPerformanceResponse> formatPerformanceResponses(List<TopicPerformance> performances);

    /**
     * Determines gap status from accuracy percentage and attempted count.
     */
    String determineGapStatus(int attempted, double accuracy);
}
