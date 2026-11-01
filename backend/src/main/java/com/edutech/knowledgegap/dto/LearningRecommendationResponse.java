package com.edutech.knowledgegap.dto;

import java.time.LocalDateTime;

public class LearningRecommendationResponse {

    private Long id;
    private Long topicId;
    private String topicName;
    private Long subjectId;
    private String subjectName;
    private Long resourceId;
    private String resourceTitle;
    private String resourceUrl;
    private String resourceContentType;
    private String recommendationText;
    private String actionType;
    private String priority;
    private String reason;
    private boolean isCompleted;
    private LocalDateTime createdAt;

    public LearningRecommendationResponse() {}

    public LearningRecommendationResponse(Long id, Long topicId, String topicName, 
                                          String recommendationText, String actionType, 
                                          String priority, String reason, boolean isCompleted, 
                                          LocalDateTime createdAt) {
        this(id, topicId, topicName, null, null, null, null, null, null,
                recommendationText, actionType, priority, reason, isCompleted, createdAt);
    }

    public LearningRecommendationResponse(Long id, Long topicId, String topicName, Long subjectId,
                                          String subjectName, Long resourceId, String resourceTitle,
                                          String resourceUrl, String resourceContentType,
                                          String recommendationText, String actionType,
                                          String priority, String reason, boolean isCompleted,
                                          LocalDateTime createdAt) {
        this.id = id;
        this.topicId = topicId;
        this.topicName = topicName;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.resourceId = resourceId;
        this.resourceTitle = resourceTitle;
        this.resourceUrl = resourceUrl;
        this.resourceContentType = resourceContentType;
        this.recommendationText = recommendationText;
        this.actionType = actionType;
        this.priority = priority;
        this.reason = reason;
        this.isCompleted = isCompleted;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public String getResourceTitle() {
        return resourceTitle;
    }

    public void setResourceTitle(String resourceTitle) {
        this.resourceTitle = resourceTitle;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getResourceContentType() {
        return resourceContentType;
    }

    public void setResourceContentType(String resourceContentType) {
        this.resourceContentType = resourceContentType;
    }

    public String getRecommendationText() {
        return recommendationText;
    }

    public void setRecommendationText(String recommendationText) {
        this.recommendationText = recommendationText;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
