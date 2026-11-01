package com.edutech.knowledgegap.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class QuizSubmissionRequest {

    /**
     * Optional client hint. The server always derives the subject from the submitted
     * question IDs and ignores any conflicting subject name.
     */
    private Long subjectId;

    /**
     * Kept for backwards compatibility with older clients. The server never trusts
     * this value for scoring or persistence.
     */
    private String subjectName;

    @NotEmpty(message = "Answers list cannot be empty")
    private List<QuestionAnswerSubmission> answers;

    public QuizSubmissionRequest() {}

    public QuizSubmissionRequest(String subjectName, List<QuestionAnswerSubmission> answers) {
        this.subjectName = subjectName;
        this.answers = answers;
    }

    public QuizSubmissionRequest(Long subjectId, List<QuestionAnswerSubmission> answers) {
        this.subjectId = subjectId;
        this.answers = answers;
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

    public List<QuestionAnswerSubmission> getAnswers() {
        return answers;
    }

    public void setAnswers(List<QuestionAnswerSubmission> answers) {
        this.answers = answers;
    }
}
