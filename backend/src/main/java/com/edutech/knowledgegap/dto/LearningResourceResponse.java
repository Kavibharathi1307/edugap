package com.edutech.knowledgegap.dto;

public class LearningResourceResponse {

    private Long id;
    private String title;
    private String contentType;
    private String resourceUrl;
    private String summary;
    private String difficultyLevel;

    public LearningResourceResponse() {}

    public LearningResourceResponse(Long id, String title, String contentType, String resourceUrl, String summary, String difficultyLevel) {
        this.id = id;
        this.title = title;
        this.contentType = contentType;
        this.resourceUrl = resourceUrl;
        this.summary = summary;
        this.difficultyLevel = difficultyLevel;
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
}
