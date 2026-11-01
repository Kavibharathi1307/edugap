package com.edutech.knowledgegap.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "topic_performances")
public class TopicPerformance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attempt_id", nullable = false)
    private QuizAttempt quizAttempt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "topic_id", nullable = false)
    private Topic topic;

    @Column(nullable = false)
    private int questionsAttempted;

    @Column(nullable = false)
    private int correctAnswers;

    @Column(nullable = false)
    private double accuracyPercentage;

    @Column(nullable = false, length = 30)
    private String gapStatus; // HIGH_PRIORITY_GAP, NEEDS_IMPROVEMENT, GOOD_UNDERSTANDING, STRONG_UNDERSTANDING, NOT_ASSESSED

    @Column(nullable = false)
    private LocalDateTime recordedAt = LocalDateTime.now();

    public TopicPerformance() {}

    public TopicPerformance(QuizAttempt quizAttempt, Student student, Topic topic, 
                            int questionsAttempted, int correctAnswers, double accuracyPercentage, String gapStatus) {
        this.quizAttempt = quizAttempt;
        this.student = student;
        this.topic = topic;
        this.questionsAttempted = questionsAttempted;
        this.correctAnswers = correctAnswers;
        this.accuracyPercentage = accuracyPercentage;
        this.gapStatus = gapStatus;
        this.recordedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public QuizAttempt getQuizAttempt() {
        return quizAttempt;
    }

    public void setQuizAttempt(QuizAttempt quizAttempt) {
        this.quizAttempt = quizAttempt;
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

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }
}
