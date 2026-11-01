package com.edutech.knowledgegap.repository;

import com.edutech.knowledgegap.entity.QuizAttempt;
import com.edutech.knowledgegap.entity.Student;
import com.edutech.knowledgegap.entity.Topic;
import com.edutech.knowledgegap.entity.TopicPerformance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest(properties = {
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
class TopicPerformanceRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TopicRepository topicRepository;

    @Autowired
    private QuizAttemptRepository attemptRepository;

    @Autowired
    private TopicPerformanceRepository performanceRepository;

    @Test
    @DisplayName("Should return the highest-id performance when timestamps are equal")
    void testFindLatestPerformancesUsesIdTieBreaker() {
        Student student = studentRepository.save(new Student("Priya Sharma", "priya@edugap.ac.in", "encoded"));
        Topic topic = topicRepository.save(new Topic("Arrays", "desc", "objectives", "beginner",
                "mistakes", "example", 1));
        QuizAttempt attempt = attemptRepository.save(new QuizAttempt(student, "Core Java", 4, 2, 50.0));

        LocalDateTime recordedAt = LocalDateTime.of(2026, 10, 31, 10, 0, 0);

        TopicPerformance older = new TopicPerformance(attempt, student, topic, 4, 1, 25.0,
                "HIGH_PRIORITY_GAP");
        older.setRecordedAt(recordedAt);
        performanceRepository.saveAndFlush(older);

        TopicPerformance newer = new TopicPerformance(attempt, student, topic, 4, 4, 100.0,
                "STRONG_UNDERSTANDING");
        newer.setRecordedAt(recordedAt);
        performanceRepository.saveAndFlush(newer);

        List<TopicPerformance> latest = performanceRepository.findLatestPerformancesByStudentId(student.getId());

        assertEquals(1, latest.size());
        assertEquals(newer.getId(), latest.get(0).getId());
        assertEquals(100.0, latest.get(0).getAccuracyPercentage());
    }
}
