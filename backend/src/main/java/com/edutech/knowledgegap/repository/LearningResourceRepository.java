package com.edutech.knowledgegap.repository;

import com.edutech.knowledgegap.entity.LearningResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningResourceRepository extends JpaRepository<LearningResource, Long> {
    List<LearningResource> findByTopicId(Long topicId);
    List<LearningResource> findByTopicIdAndDifficultyLevel(Long topicId, String difficultyLevel);
}
