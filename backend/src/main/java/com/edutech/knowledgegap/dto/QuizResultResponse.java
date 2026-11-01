package com.edutech.knowledgegap.dto;

import java.time.LocalDateTime;
import java.util.List;

public class QuizResultResponse {

    private Long attemptId;
    private Long subjectId;
    private String subjectName;
    private int totalQuestions;
    private int correctAnswers;
    private double scorePercentage;
    private LocalDateTime completedAt;
    private List<TopicPerformanceResponse> topicPerformances;
    private List<AnswerReviewResponse> answersReview;
    private List<LearningRecommendationResponse> recommendations;

    public QuizResultResponse() {}

    public QuizResultResponse(Long attemptId, String subjectName, int totalQuestions, 
                              int correctAnswers, double scorePercentage, LocalDateTime completedAt, 
                              List<TopicPerformanceResponse> topicPerformances, 
                              List<AnswerReviewResponse> answersReview, 
                              List<LearningRecommendationResponse> recommendations) {
        this(attemptId, null, subjectName, totalQuestions, correctAnswers, scorePercentage,
                completedAt, topicPerformances, answersReview, recommendations);
    }

    public QuizResultResponse(Long attemptId, Long subjectId, String subjectName, int totalQuestions,
                              int correctAnswers, double scorePercentage, LocalDateTime completedAt,
                              List<TopicPerformanceResponse> topicPerformances,
                              List<AnswerReviewResponse> answersReview,
                              List<LearningRecommendationResponse> recommendations) {
        this.attemptId = attemptId;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.scorePercentage = scorePercentage;
        this.completedAt = completedAt;
        this.topicPerformances = topicPerformances;
        this.answersReview = answersReview;
        this.recommendations = recommendations;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public Long getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(Long attemptId) {
        this.attemptId = attemptId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public double getScorePercentage() {
        return scorePercentage;
    }

    public void setScorePercentage(double scorePercentage) {
        this.scorePercentage = scorePercentage;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public List<TopicPerformanceResponse> getTopicPerformances() {
        return topicPerformances;
    }

    public void setTopicPerformances(List<TopicPerformanceResponse> topicPerformances) {
        this.topicPerformances = topicPerformances;
    }

    public List<AnswerReviewResponse> getAnswersReview() {
        return answersReview;
    }

    public void setAnswersReview(List<AnswerReviewResponse> answersReview) {
        this.answersReview = answersReview;
    }

    public List<LearningRecommendationResponse> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<LearningRecommendationResponse> recommendations) {
        this.recommendations = recommendations;
    }
}
