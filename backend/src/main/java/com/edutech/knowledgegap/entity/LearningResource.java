package com.edutech.knowledgegap.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "learning_resources")
public class LearningResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id", nullable = false)
    @JsonIgnore
    private Topic topic;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 30)
    private String contentType = "ARTICLE"; // ARTICLE, VIDEO, CODE_EXAMPLE, CHEATSHEET

    @Column(columnDefinition = "TEXT")
    private String resourceUrl;

    @Column(columnDefinition = "TEXT")
    private String summary;

    @Column(nullable = false, length = 20)
    private String difficultyLevel = "BEGINNER"; // BEGINNER, INTERMEDIATE, ADVANCED

    public LearningResource() {}

    public LearningResource(Topic topic, String title, String contentType, String resourceUrl, String summary, String difficultyLevel) {
        this.topic = topic;
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

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
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
