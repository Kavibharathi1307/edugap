package com.edutech.knowledgegap.dto;

import java.time.LocalDateTime;

public class SubjectProgressResponse {

    private Long subjectId;
    private String subjectCode;
    private String subjectName;
    private long topicCount;
    private long assessedTopicCount;
    private long attemptCount;
    private double averageScore;
    private Double latestScore;
    private Double previousScore;
    private Double scoreChange;
    private Double improvementPercentage;
    private double overallProgressPercentage;
    private LocalDateTime lastAttemptAt;

    public SubjectProgressResponse() {}

    public SubjectProgressResponse(Long subjectId, String subjectCode, String subjectName,
                                   long topicCount, long assessedTopicCount, long attemptCount,
                                   double averageScore, Double latestScore, Double previousScore,
                                   Double scoreChange, Double improvementPercentage,
                                   double overallProgressPercentage, LocalDateTime lastAttemptAt) {
        this.subjectId = subjectId;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.topicCount = topicCount;
        this.assessedTopicCount = assessedTopicCount;
        this.attemptCount = attemptCount;
        this.averageScore = averageScore;
        this.latestScore = latestScore;
        this.previousScore = previousScore;
        this.scoreChange = scoreChange;
        this.improvementPercentage = improvementPercentage;
        this.overallProgressPercentage = overallProgressPercentage;
        this.lastAttemptAt = lastAttemptAt;
    }

    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public long getTopicCount() { return topicCount; }
    public void setTopicCount(long topicCount) { this.topicCount = topicCount; }
    public long getAssessedTopicCount() { return assessedTopicCount; }
    public void setAssessedTopicCount(long assessedTopicCount) { this.assessedTopicCount = assessedTopicCount; }
    public long getAttemptCount() { return attemptCount; }
    public void setAttemptCount(long attemptCount) { this.attemptCount = attemptCount; }
    public double getAverageScore() { return averageScore; }
    public void setAverageScore(double averageScore) { this.averageScore = averageScore; }
    public Double getLatestScore() { return latestScore; }
    public void setLatestScore(Double latestScore) { this.latestScore = latestScore; }
    public Double getPreviousScore() { return previousScore; }
    public void setPreviousScore(Double previousScore) { this.previousScore = previousScore; }
    public Double getScoreChange() { return scoreChange; }
    public void setScoreChange(Double scoreChange) { this.scoreChange = scoreChange; }
    public Double getImprovementPercentage() { return improvementPercentage; }
    public void setImprovementPercentage(Double improvementPercentage) { this.improvementPercentage = improvementPercentage; }
    public double getOverallProgressPercentage() { return overallProgressPercentage; }
    public void setOverallProgressPercentage(double overallProgressPercentage) { this.overallProgressPercentage = overallProgressPercentage; }
    public LocalDateTime getLastAttemptAt() { return lastAttemptAt; }
    public void setLastAttemptAt(LocalDateTime lastAttemptAt) { this.lastAttemptAt = lastAttemptAt; }
}
