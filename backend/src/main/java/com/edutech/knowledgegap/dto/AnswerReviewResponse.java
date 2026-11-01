package com.edutech.knowledgegap.dto;

public class AnswerReviewResponse {

    private Long questionId;
    private String questionText;
    private String topicName;
    private String selectedOptionKey;
    private String correctOptionKey;
    private boolean isCorrect;
    private String explanation;

    public AnswerReviewResponse() {}

    public AnswerReviewResponse(Long questionId, String questionText, String topicName, 
                                String selectedOptionKey, String correctOptionKey, 
                                boolean isCorrect, String explanation) {
        this.questionId = questionId;
        this.questionText = questionText;
        this.topicName = topicName;
        this.selectedOptionKey = selectedOptionKey;
        this.correctOptionKey = correctOptionKey;
        this.isCorrect = isCorrect;
        this.explanation = explanation;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getSelectedOptionKey() {
        return selectedOptionKey;
    }

    public void setSelectedOptionKey(String selectedOptionKey) {
        this.selectedOptionKey = selectedOptionKey;
    }

    public String getCorrectOptionKey() {
        return correctOptionKey;
    }

    public void setCorrectOptionKey(String correctOptionKey) {
        this.correctOptionKey = correctOptionKey;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}
