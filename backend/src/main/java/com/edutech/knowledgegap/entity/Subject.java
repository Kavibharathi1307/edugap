package com.edutech.knowledgegap.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "subjects", uniqueConstraints = {
    @UniqueConstraint(name = "uk_subjects_code", columnNames = {"code"}),
    @UniqueConstraint(name = "uk_subjects_name", columnNames = {"name"})
})
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 50)
    private String icon;

    @Column(nullable = false)
    private Integer orderIndex = 0;

    @Column(nullable = false)
    private boolean active = true;

    public Subject() {}

    public Subject(String code, String name, String description, String icon, Integer orderIndex) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.orderIndex = orderIndex;
        this.active = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
