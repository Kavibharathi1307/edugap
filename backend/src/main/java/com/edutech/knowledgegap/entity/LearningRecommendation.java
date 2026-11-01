package com.edutech.knowledgegap.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "learning_recommendations")
public class LearningRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id")
    private QuizAttempt quizAttempt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resource_id")
    private LearningResource resource;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String recommendationText;

    @Column(nullable = false, length = 30)
    private String actionType = "STUDY_TOPIC"; // STUDY_TOPIC, PRACTICE_QUIZ, CODE_EXERCISE

    @Column(nullable = false, length = 20)
    private String priority = "HIGH"; // HIGH, MEDIUM, LOW

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(nullable = false)
    private boolean isCompleted = false;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public LearningRecommendation() {}

    public LearningRecommendation(Student student, Topic topic, QuizAttempt quizAttempt, 
                                  String recommendationText, String actionType, String priority, String reason) {
        this.student = student;
        this.topic = topic;
        this.quizAttempt = quizAttempt;
        this.recommendationText = recommendationText;
        this.actionType = actionType;
        this.priority = priority;
        this.reason = reason;
        this.isCompleted = false;
        this.createdAt = LocalDateTime.now();
    }

    public LearningRecommendation(Student student, Topic topic, QuizAttempt quizAttempt,
                                  LearningResource resource, String recommendationText,
                                  String actionType, String priority, String reason) {
        this(student, topic, quizAttempt, recommendationText, actionType, priority, reason);
        this.resource = resource;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public QuizAttempt getQuizAttempt() {
        return quizAttempt;
    }

    public void setQuizAttempt(QuizAttempt quizAttempt) {
        this.quizAttempt = quizAttempt;
    }

    public LearningResource getResource() {
        return resource;
    }

    public void setResource(LearningResource resource) {
        this.resource = resource;
    }

    public String getRecommendationText() {
        return recommendationText;
    }

    public void setRecommendationText(String recommendationText) {
        this.recommendationText = recommendationText;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
