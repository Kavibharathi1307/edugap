package com.edutech.knowledgegap.repository;

import com.edutech.knowledgegap.entity.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    List<QuizAttempt> findByStudentIdOrderByCompletedAtDesc(Long studentId);
    Optional<QuizAttempt> findByIdAndStudentId(Long id, Long studentId);
    List<QuizAttempt> findTop5ByStudentIdOrderByCompletedAtDesc(Long studentId);
    long countByStudentId(Long studentId);
    List<QuizAttempt> findByStudentIdAndSubjectIdOrderByCompletedAtDesc(Long studentId, Long subjectId);
    long countByStudentIdAndSubjectId(Long studentId, Long subjectId);
    List<QuizAttempt> findAllBySubjectIsNull();
}
