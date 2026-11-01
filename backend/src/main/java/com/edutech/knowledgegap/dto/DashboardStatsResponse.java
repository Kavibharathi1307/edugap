package com.edutech.knowledgegap.dto;

import java.util.List;
import java.util.Map;

public class DashboardStatsResponse {

    private long totalAssessmentsTaken;
    private double averageScore;
    private long completedTopicsCount;
    private long totalTopicsCount;
    private long activeRecommendationsCount;
    private List<TopicPerformanceResponse> strongestTopics;
    private List<TopicPerformanceResponse> weakestTopics;
    private List<TopicPerformanceResponse> unassessedTopics;
    private List<TopicPerformanceResponse> allTopicsPerformance;
    private List<Map<String, Object>> recentAttempts;
    private List<SubjectProgressResponse> subjectProgress;
    private long assessedSubjectsCount;
    private long totalSubjectsCount;
    private double overallProgressPercentage;
    private Double improvementPercentage;

    public DashboardStatsResponse() {}

    public DashboardStatsResponse(long totalAssessmentsTaken, double averageScore, 
                                  long completedTopicsCount, long totalTopicsCount, 
                                  long activeRecommendationsCount, 
                                  List<TopicPerformanceResponse> strongestTopics, 
                                  List<TopicPerformanceResponse> weakestTopics, 
                                  List<TopicPerformanceResponse> unassessedTopics, 
                                  List<TopicPerformanceResponse> allTopicsPerformance, 
                                  List<Map<String, Object>> recentAttempts) {
        this(totalAssessmentsTaken, averageScore, completedTopicsCount, totalTopicsCount,
                activeRecommendationsCount, strongestTopics, weakestTopics, unassessedTopics,
                allTopicsPerformance, recentAttempts, List.of(), 0, 0, 0.0, null);
    }

    public DashboardStatsResponse(long totalAssessmentsTaken, double averageScore,
                                  long completedTopicsCount, long totalTopicsCount,
                                  long activeRecommendationsCount,
                                  List<TopicPerformanceResponse> strongestTopics,
                                  List<TopicPerformanceResponse> weakestTopics,
                                  List<TopicPerformanceResponse> unassessedTopics,
                                  List<TopicPerformanceResponse> allTopicsPerformance,
                                  List<Map<String, Object>> recentAttempts,
                                  List<SubjectProgressResponse> subjectProgress,
                                  long assessedSubjectsCount, long totalSubjectsCount,
                                  double overallProgressPercentage, Double improvementPercentage) {
        this.totalAssessmentsTaken = totalAssessmentsTaken;
        this.averageScore = averageScore;
        this.completedTopicsCount = completedTopicsCount;
        this.totalTopicsCount = totalTopicsCount;
        this.activeRecommendationsCount = activeRecommendationsCount;
        this.strongestTopics = strongestTopics;
        this.weakestTopics = weakestTopics;
        this.unassessedTopics = unassessedTopics;
        this.allTopicsPerformance = allTopicsPerformance;
        this.recentAttempts = recentAttempts;
        this.subjectProgress = subjectProgress;
        this.assessedSubjectsCount = assessedSubjectsCount;
        this.totalSubjectsCount = totalSubjectsCount;
        this.overallProgressPercentage = overallProgressPercentage;
        this.improvementPercentage = improvementPercentage;
    }

    public long getTotalAssessmentsTaken() {
        return totalAssessmentsTaken;
    }

    public void setTotalAssessmentsTaken(long totalAssessmentsTaken) {
        this.totalAssessmentsTaken = totalAssessmentsTaken;
    }

    public double getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(double averageScore) {
        this.averageScore = averageScore;
    }

    public long getCompletedTopicsCount() {
        return completedTopicsCount;
    }

    public void setCompletedTopicsCount(long completedTopicsCount) {
        this.completedTopicsCount = completedTopicsCount;
    }

    public long getTotalTopicsCount() {
        return totalTopicsCount;
    }

    public void setTotalTopicsCount(long totalTopicsCount) {
        this.totalTopicsCount = totalTopicsCount;
    }

    public long getActiveRecommendationsCount() {
        return activeRecommendationsCount;
    }

    public void setActiveRecommendationsCount(long activeRecommendationsCount) {
        this.activeRecommendationsCount = activeRecommendationsCount;
    }

    public List<TopicPerformanceResponse> getStrongestTopics() {
        return strongestTopics;
    }

    public void setStrongestTopics(List<TopicPerformanceResponse> strongestTopics) {
        this.strongestTopics = strongestTopics;
    }

    public List<TopicPerformanceResponse> getWeakestTopics() {
        return weakestTopics;
    }

    public void setWeakestTopics(List<TopicPerformanceResponse> weakestTopics) {
        this.weakestTopics = weakestTopics;
    }

    public List<TopicPerformanceResponse> getUnassessedTopics() {
        return unassessedTopics;
    }

    public void setUnassessedTopics(List<TopicPerformanceResponse> unassessedTopics) {
        this.unassessedTopics = unassessedTopics;
    }

    public List<TopicPerformanceResponse> getAllTopicsPerformance() {
        return allTopicsPerformance;
    }

    public void setAllTopicsPerformance(List<TopicPerformanceResponse> allTopicsPerformance) {
        this.allTopicsPerformance = allTopicsPerformance;
    }

    public List<Map<String, Object>> getRecentAttempts() {
        return recentAttempts;
    }

    public void setRecentAttempts(List<Map<String, Object>> recentAttempts) {
        this.recentAttempts = recentAttempts;
    }

    public List<SubjectProgressResponse> getSubjectProgress() {
        return subjectProgress;
    }

    public void setSubjectProgress(List<SubjectProgressResponse> subjectProgress) {
        this.subjectProgress = subjectProgress;
    }

    public long getAssessedSubjectsCount() {
        return assessedSubjectsCount;
    }

    public void setAssessedSubjectsCount(long assessedSubjectsCount) {
        this.assessedSubjectsCount = assessedSubjectsCount;
    }

    public long getTotalSubjectsCount() {
        return totalSubjectsCount;
    }

    public void setTotalSubjectsCount(long totalSubjectsCount) {
        this.totalSubjectsCount = totalSubjectsCount;
    }

    public double getOverallProgressPercentage() {
        return overallProgressPercentage;
    }

    public void setOverallProgressPercentage(double overallProgressPercentage) {
        this.overallProgressPercentage = overallProgressPercentage;
    }

    public Double getImprovementPercentage() {
        return improvementPercentage;
    }

    public void setImprovementPercentage(Double improvementPercentage) {
        this.improvementPercentage = improvementPercentage;
    }
}
