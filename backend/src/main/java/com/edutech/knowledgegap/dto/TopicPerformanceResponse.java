package com.edutech.knowledgegap.dto;

public class TopicPerformanceResponse {

    private Long topicId;
    private String topicName;
    private Long subjectId;
    private String subjectName;
    private int questionsAttempted;
    private int correctAnswers;
    private double accuracyPercentage;
    private String gapStatus; // HIGH_PRIORITY_GAP, NEEDS_IMPROVEMENT, GOOD_UNDERSTANDING, STRONG_UNDERSTANDING, NOT_ASSESSED
    private String gapLabel;
    private String badgeColor;

    public TopicPerformanceResponse() {}

    public TopicPerformanceResponse(Long topicId, String topicName, int questionsAttempted, 
                                    int correctAnswers, double accuracyPercentage, String gapStatus, 
                                    String gapLabel, String badgeColor) {
        this(topicId, topicName, null, null, questionsAttempted, correctAnswers,
                accuracyPercentage, gapStatus, gapLabel, badgeColor);
    }

    public TopicPerformanceResponse(Long topicId, String topicName, Long subjectId, String subjectName,
                                    int questionsAttempted, int correctAnswers, double accuracyPercentage,
                                    String gapStatus, String gapLabel, String badgeColor) {
        this.topicId = topicId;
        this.topicName = topicName;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.questionsAttempted = questionsAttempted;
        this.correctAnswers = correctAnswers;
        this.accuracyPercentage = accuracyPercentage;
        this.gapStatus = gapStatus;
        this.gapLabel = gapLabel;
        this.badgeColor = badgeColor;
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

    public int getQuestionsAttempted() {
        return questionsAttempted;
    }

    public void setQuestionsAttempted(int questionsAttempted) {
        this.questionsAttempted = questionsAttempted;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public double getAccuracyPercentage() {
        return accuracyPercentage;
    }

    public void setAccuracyPercentage(double accuracyPercentage) {
        this.accuracyPercentage = accuracyPercentage;
    }

    public String getGapStatus() {
        return gapStatus;
    }

    public void setGapStatus(String gapStatus) {
        this.gapStatus = gapStatus;
    }

    public String getGapLabel() {
        return gapLabel;
    }

    public void setGapLabel(String gapLabel) {
        this.gapLabel = gapLabel;
    }

    public String getBadgeColor() {
        return badgeColor;
    }

    public void setBadgeColor(String badgeColor) {
        this.badgeColor = badgeColor;
    }
}
