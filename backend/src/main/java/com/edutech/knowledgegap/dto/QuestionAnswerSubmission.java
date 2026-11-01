package com.edutech.knowledgegap.dto;

import jakarta.validation.constraints.NotNull;

public class QuestionAnswerSubmission {

    @NotNull(message = "Question ID is required")
    private Long questionId;

    private String selectedOptionKey; // A, B, C, D (or null if skipped)

    public QuestionAnswerSubmission() {}

    public QuestionAnswerSubmission(Long questionId, String selectedOptionKey) {
        this.questionId = questionId;
        this.selectedOptionKey = selectedOptionKey;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getSelectedOptionKey() {
        return selectedOptionKey;
    }

    public void setSelectedOptionKey(String selectedOptionKey) {
        this.selectedOptionKey = selectedOptionKey;
    }
}
