package com.edutech.knowledgegap.repository;

import com.edutech.knowledgegap.entity.LearningResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LearningResourceRepository extends JpaRepository<LearningResource, Long> {
    List<LearningResource> findByTopicId(Long topicId);
    List<LearningResource> findByTopicIdAndDifficultyLevel(Long topicId, String difficultyLevel);

    @Query("SELECT r FROM LearningResource r JOIN FETCH r.topic t LEFT JOIN FETCH t.subject ORDER BY t.orderIndex ASC, r.id ASC")
    List<LearningResource> findAllWithTopic();

    @Query("SELECT r FROM LearningResource r JOIN FETCH r.topic t LEFT JOIN FETCH t.subject WHERE r.id = :resourceId")
    Optional<LearningResource> findByIdWithTopic(@Param("resourceId") Long resourceId);
}
