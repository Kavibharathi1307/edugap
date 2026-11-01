package com.edutech.knowledgegap.repository;

import com.edutech.knowledgegap.entity.TopicPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicPerformanceRepository extends JpaRepository<TopicPerformance, Long> {
    List<TopicPerformance> findByQuizAttemptId(Long quizAttemptId);
    List<TopicPerformance> findByStudentIdOrderByRecordedAtDesc(Long studentId);
    
    @Query("SELECT tp FROM TopicPerformance tp WHERE tp.student.id = :studentId AND tp.id = " +
           "(SELECT MAX(tp2.id) FROM TopicPerformance tp2 WHERE tp2.student.id = :studentId " +
           "AND tp2.topic.id = tp.topic.id AND tp2.recordedAt = " +
           "(SELECT MAX(tp3.recordedAt) FROM TopicPerformance tp3 WHERE tp3.student.id = :studentId " +
           "AND tp3.topic.id = tp.topic.id))")
    List<TopicPerformance> findLatestPerformancesByStudentId(Long studentId);

    List<TopicPerformance> findByStudentIdAndTopicIdOrderByRecordedAtDesc(Long studentId, Long topicId);
}
