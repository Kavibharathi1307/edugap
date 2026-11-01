package com.edutech.knowledgegap.service;

import com.edutech.knowledgegap.dto.LearningRecommendationResponse;
import com.edutech.knowledgegap.entity.QuizAttempt;
import com.edutech.knowledgegap.entity.Student;
import com.edutech.knowledgegap.entity.TopicPerformance;

import java.util.List;

public interface PersonalizedRecommendationService {

    /**
     * Generates personalized learning recommendations based on the detected knowledge gaps
     * and historical delta comparisons.
     */
    List<LearningRecommendationResponse> generateRecommendations(
            QuizAttempt attempt,
            Student student,
            List<TopicPerformance> performances
    );

    /**
     * Retrieves all active (uncompleted) recommendations for the student.
     */
    List<LearningRecommendationResponse> getActiveRecommendations(Student student);

    /**
     * Marks a recommendation as completed.
     */
    LearningRecommendationResponse markAsCompleted(Long recommendationId, Student student);
}
