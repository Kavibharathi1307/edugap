package com.edutech.knowledgegap.dto;

public class LearningResourceResponse {

    private Long id;
    private String title;
    private String contentType;
    private String resourceUrl;
    private String summary;
    private String difficultyLevel;
    private Long topicId;
    private String topicName;
    private Long subjectId;
    private String subjectName;

    public LearningResourceResponse() {}

    public LearningResourceResponse(Long id, String title, String contentType, String resourceUrl, String summary, String difficultyLevel) {
        this.id = id;
        this.title = title;
        this.contentType = contentType;
        this.resourceUrl = resourceUrl;
        this.summary = summary;
        this.difficultyLevel = difficultyLevel;
    }

    public LearningResourceResponse(Long id, String title, String contentType, String resourceUrl, String summary,
                                     String difficultyLevel, Long topicId, String topicName,
                                     Long subjectId, String subjectName) {
        this(id, title, contentType, resourceUrl, summary, difficultyLevel);
        this.topicId = topicId;
        this.topicName = topicName;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
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
}
