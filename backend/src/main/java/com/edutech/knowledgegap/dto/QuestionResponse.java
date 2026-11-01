package com.edutech.knowledgegap.dto;

import java.util.List;

public class QuestionResponse {

    private Long id;
    private Long topicId;
    private String topicName;
    private Long subjectId;
    private String subjectName;
    private String questionText;
    private String difficultyLevel;
    private List<QuestionOptionResponse> options;

    public QuestionResponse() {}

    public QuestionResponse(Long id, Long topicId, String topicName, String questionText, 
                            String difficultyLevel, List<QuestionOptionResponse> options) {
        this(id, topicId, topicName, null, null, questionText, difficultyLevel, options);
    }

    public QuestionResponse(Long id, Long topicId, String topicName, Long subjectId,
                            String subjectName, String questionText, String difficultyLevel,
                            List<QuestionOptionResponse> options) {
        this.id = id;
        this.topicId = topicId;
        this.topicName = topicName;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.questionText = questionText;
        this.difficultyLevel = difficultyLevel;
        this.options = options;
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

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public List<QuestionOptionResponse> getOptions() {
        return options;
    }

    public void setOptions(List<QuestionOptionResponse> options) {
        this.options = options;
    }
}
