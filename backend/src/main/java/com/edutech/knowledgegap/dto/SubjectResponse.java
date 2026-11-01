package com.edutech.knowledgegap.dto;

import java.time.LocalDateTime;

public class SubjectResponse {

    private Long id;
    private String code;
    private String name;
    private String description;
    private String icon;
    private Integer orderIndex;
    private long topicCount;
    private long assessedTopicCount;
    private long attemptCount;
    private double averageScore;
    private Double latestScore;
    private LocalDateTime lastAttemptAt;

    public SubjectResponse() {}

    public SubjectResponse(Long id, String code, String name, String description, String icon,
                           Integer orderIndex, long topicCount, long assessedTopicCount,
                           long attemptCount, double averageScore, Double latestScore,
                           LocalDateTime lastAttemptAt) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.orderIndex = orderIndex;
        this.topicCount = topicCount;
        this.assessedTopicCount = assessedTopicCount;
        this.attemptCount = attemptCount;
        this.averageScore = averageScore;
        this.latestScore = latestScore;
        this.lastAttemptAt = lastAttemptAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }
    public Integer getOrderIndex() { return orderIndex; }
    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }
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
    public LocalDateTime getLastAttemptAt() { return lastAttemptAt; }
    public void setLastAttemptAt(LocalDateTime lastAttemptAt) { this.lastAttemptAt = lastAttemptAt; }
}
