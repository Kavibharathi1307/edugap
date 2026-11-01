package com.edutech.knowledgegap.service;

import com.edutech.knowledgegap.config.PerformanceThresholds;
import com.edutech.knowledgegap.entity.QuizAttempt;
import com.edutech.knowledgegap.entity.Student;
import com.edutech.knowledgegap.entity.Topic;
import com.edutech.knowledgegap.entity.TopicPerformance;
import com.edutech.knowledgegap.repository.TopicPerformanceRepository;
import com.edutech.knowledgegap.repository.TopicRepository;
import com.edutech.knowledgegap.service.impl.KnowledgeGapDetectionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KnowledgeGapDetectionServiceTest {

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private TopicPerformanceRepository performanceRepository;

    @Mock
    private PerformanceThresholds thresholds;

    @InjectMocks
    private KnowledgeGapDetectionServiceImpl gapService;

    private Topic topic1;
    private Topic topic2;
    private Topic topic5;

    @BeforeEach
    void setUp() {
        when(thresholds.getHighPriorityThreshold()).thenReturn(40.0);
        when(thresholds.getNeedsImprovementThreshold()).thenReturn(70.0);
        when(thresholds.getGoodUnderstandingThreshold()).thenReturn(85.0);

        topic1 = new Topic("Java Fundamentals", "", "", "", "", "", 1);
        topic1.setId(1L);
        topic2 = new Topic("Arrays", "", "", "", "", "", 2);
        topic2.setId(2L);
        topic5 = new Topic("Unassessed Topic", "", "", "", "", "", 5);
        topic5.setId(5L);
    }

    @Test
    @DisplayName("Should correctly classify gap severity at and around threshold boundaries")
    void testDetermineGapStatusBoundaries() {
        // No assessment history
        assertEquals("NOT_ASSESSED", gapService.determineGapStatus(0, 0.0));
        assertEquals("NOT_ASSESSED", gapService.determineGapStatus(-1, 0.0));

        // High priority gap: below 40%
        assertEquals("HIGH_PRIORITY_GAP", gapService.determineGapStatus(4, 0.0));
        assertEquals("HIGH_PRIORITY_GAP", gapService.determineGapStatus(4, 39.9));

        // Needs improvement: 40% to below 70%
        assertEquals("NEEDS_IMPROVEMENT", gapService.determineGapStatus(4, 40.0));
        assertEquals("NEEDS_IMPROVEMENT", gapService.determineGapStatus(4, 69.9));

        // Good understanding: 70% to below 85%
        assertEquals("GOOD_UNDERSTANDING", gapService.determineGapStatus(4, 70.0));
        assertEquals("GOOD_UNDERSTANDING", gapService.determineGapStatus(4, 84.9));

        // Strong understanding: 85% and above
        assertEquals("STRONG_UNDERSTANDING", gapService.determineGapStatus(4, 85.0));
        assertEquals("STRONG_UNDERSTANDING", gapService.determineGapStatus(4, 100.0));
    }

    @Test
    @DisplayName("Should analyze attempted topics and persist TopicPerformance records")
    void testAnalyzeAndPersistGaps() {
        when(topicRepository.findAllById(any())).thenReturn(List.of(topic1, topic2, topic5));
        when(performanceRepository.save(any(TopicPerformance.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Student student = new Student("Test Student", "test@edugap.ac.in", "pass");
        student.setId(10L);
        QuizAttempt attempt = new QuizAttempt(student, "Core Java", 8, 5, 62.5);
        attempt.setId(100L);

        // Topic 1: 4 attempted, 4 correct (100% -> STRONG_UNDERSTANDING)
        // Topic 2: 4 attempted, 1 correct (25% -> HIGH_PRIORITY_GAP)
        // Topic 5: 0 attempted (excluded from persisted performances)
        Map<Long, Integer> totalMap = Map.of(1L, 4, 2L, 4, 5L, 0);
        Map<Long, Integer> correctMap = Map.of(1L, 4, 2L, 1);

        List<TopicPerformance> results = gapService.analyzeAndPersistGaps(attempt, student, totalMap, correctMap);

        assertEquals(2, results.size());

        TopicPerformance p1 = results.stream().filter(p -> p.getTopic().getId().equals(1L)).findFirst().orElseThrow();
        assertEquals(100.0, p1.getAccuracyPercentage());
        assertEquals("STRONG_UNDERSTANDING", p1.getGapStatus());

        TopicPerformance p2 = results.stream().filter(p -> p.getTopic().getId().equals(2L)).findFirst().orElseThrow();
        assertEquals(25.0, p2.getAccuracyPercentage());
        assertEquals("HIGH_PRIORITY_GAP", p2.getGapStatus());

        verify(performanceRepository, times(2)).save(any(TopicPerformance.class));
    }
}
