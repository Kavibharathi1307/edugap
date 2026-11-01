package com.edutech.knowledgegap.dto;

public class TopicSummaryResponse {

    private Long id;
    private String name;
    private String description;
    private Integer orderIndex;
    private Long subjectId;
    private String subjectName;
    private long questionCount;
    private boolean assessed;
    private boolean completed;
    private int questionsAttempted;
    private int correctAnswers;
    private double accuracyPercentage;
    private String gapStatus;
    private String gapLabel;
    private String badgeColor;

    public TopicSummaryResponse() {}

    public TopicSummaryResponse(Long id, String name, String description, Integer orderIndex,
                                Long subjectId, String subjectName, long questionCount,
                                boolean assessed, boolean completed, int questionsAttempted,
                                int correctAnswers, double accuracyPercentage, String gapStatus,
                                String gapLabel, String badgeColor) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.orderIndex = orderIndex;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.questionCount = questionCount;
        this.assessed = assessed;
        this.completed = completed;
        this.questionsAttempted = questionsAttempted;
        this.correctAnswers = correctAnswers;
        this.accuracyPercentage = accuracyPercentage;
        this.gapStatus = gapStatus;
        this.gapLabel = gapLabel;
        this.badgeColor = badgeColor;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getOrderIndex() { return orderIndex; }
    public void setOrderIndex(Integer orderIndex) { this.orderIndex = orderIndex; }
    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    public long getQuestionCount() { return questionCount; }
    public void setQuestionCount(long questionCount) { this.questionCount = questionCount; }
    public boolean isAssessed() { return assessed; }
    public void setAssessed(boolean assessed) { this.assessed = assessed; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public int getQuestionsAttempted() { return questionsAttempted; }
    public void setQuestionsAttempted(int questionsAttempted) { this.questionsAttempted = questionsAttempted; }
    public int getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(int correctAnswers) { this.correctAnswers = correctAnswers; }
    public double getAccuracyPercentage() { return accuracyPercentage; }
    public void setAccuracyPercentage(double accuracyPercentage) { this.accuracyPercentage = accuracyPercentage; }
    public String getGapStatus() { return gapStatus; }
    public void setGapStatus(String gapStatus) { this.gapStatus = gapStatus; }
    public String getGapLabel() { return gapLabel; }
    public void setGapLabel(String gapLabel) { this.gapLabel = gapLabel; }
    public String getBadgeColor() { return badgeColor; }
    public void setBadgeColor(String badgeColor) { this.badgeColor = badgeColor; }
}
