package com.edutech.knowledgegap.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "topics")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String learningObjectives;

    @Column(columnDefinition = "TEXT")
    private String beginnerExplanation;

    @Column(columnDefinition = "TEXT")
    private String commonMistakes;

    @Column(columnDefinition = "TEXT")
    private String codeExample;

    private Integer orderIndex = 0;

    @Column(nullable = false, length = 50)
    private String subjectName = "Java Programming";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @OneToMany(mappedBy = "topic", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LearningResource> learningResources = new ArrayList<>();

    public Topic() {}

    public Topic(String name, String description, String learningObjectives, 
                 String beginnerExplanation, String commonMistakes, String codeExample, int orderIndex) {
        this.name = name;
        this.description = description;
        this.learningObjectives = learningObjectives;
        this.beginnerExplanation = beginnerExplanation;
        this.commonMistakes = commonMistakes;
        this.codeExample = codeExample;
        this.orderIndex = orderIndex;
        this.subjectName = "Java Programming";
    }

    public Topic(Subject subject, String name, String description, String learningObjectives,
                 String beginnerExplanation, String commonMistakes, String codeExample, int orderIndex) {
        this(name, description, learningObjectives, beginnerExplanation, commonMistakes, codeExample, orderIndex);
        this.subject = subject;
        if (subject != null) {
            this.subjectName = subject.getName();
        }
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

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
        if (subject != null) {
            this.subjectName = subject.getName();
        }
    }

    public List<LearningResource> getLearningResources() {
        return learningResources;
    }

    public void setLearningResources(List<LearningResource> learningResources) {
        this.learningResources = learningResources;
    }
}
