package com.edutech.knowledgegap.dto;

import java.util.List;

public class TopicResponse {

    private Long id;
    private String name;
    private String description;
    private String learningObjectives;
    private String beginnerExplanation;
    private String commonMistakes;
    private String codeExample;
    private Integer orderIndex;
    private Long subjectId;
    private String subjectCode;
    private String subjectName;
    private boolean isCompleted;
    private List<LearningResourceResponse> resources;

    public TopicResponse() {}

    public TopicResponse(Long id, String name, String description, String learningObjectives, 
                         String beginnerExplanation, String commonMistakes, String codeExample, 
                         Integer orderIndex, String subjectName, boolean isCompleted, 
                         List<LearningResourceResponse> resources) {
        this(id, name, description, learningObjectives, beginnerExplanation, commonMistakes,
                codeExample, orderIndex, null, null, subjectName, isCompleted, resources);
    }

    public TopicResponse(Long id, String name, String description, String learningObjectives,
                         String beginnerExplanation, String commonMistakes, String codeExample,
                         Integer orderIndex, Long subjectId, String subjectCode, String subjectName,
                         boolean isCompleted, List<LearningResourceResponse> resources) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.learningObjectives = learningObjectives;
        this.beginnerExplanation = beginnerExplanation;
        this.commonMistakes = commonMistakes;
        this.codeExample = codeExample;
        this.orderIndex = orderIndex;
        this.subjectId = subjectId;
        this.subjectCode = subjectCode;
        this.subjectName = subjectName;
        this.isCompleted = isCompleted;
        this.resources = resources;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLearningObjectives() {
        return learningObjectives;
    }

    public void setLearningObjectives(String learningObjectives) {
        this.learningObjectives = learningObjectives;
    }

    public String getBeginnerExplanation() {
        return beginnerExplanation;
    }

    public void setBeginnerExplanation(String beginnerExplanation) {
        this.beginnerExplanation = beginnerExplanation;
    }

    public String getCommonMistakes() {
        return commonMistakes;
    }

    public void setCommonMistakes(String commonMistakes) {
        this.commonMistakes = commonMistakes;
    }

    public String getCodeExample() {
        return codeExample;
    }

    public void setCodeExample(String codeExample) {
        this.codeExample = codeExample;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public Long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(Long subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public void setSubjectCode(String subjectCode) {
        this.subjectCode = subjectCode;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public List<LearningResourceResponse> getResources() {
        return resources;
    }

    public void setResources(List<LearningResourceResponse> resources) {
        this.resources = resources;
    }
}
