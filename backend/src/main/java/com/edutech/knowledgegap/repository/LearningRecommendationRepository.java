package com.edutech.knowledgegap.repository;

import com.edutech.knowledgegap.entity.LearningRecommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LearningRecommendationRepository extends JpaRepository<LearningRecommendation, Long> {
    List<LearningRecommendation> findByStudentIdOrderByCreatedAtDesc(Long studentId);
    List<LearningRecommendation> findByStudentIdAndIsCompletedFalseOrderByPriorityAsc(Long studentId);
    Optional<LearningRecommendation> findByIdAndStudentId(Long id, Long studentId);
}
