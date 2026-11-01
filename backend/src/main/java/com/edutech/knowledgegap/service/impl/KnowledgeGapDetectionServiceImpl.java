package com.edutech.knowledgegap.service.impl;

import com.edutech.knowledgegap.config.PerformanceThresholds;
import com.edutech.knowledgegap.dto.TopicPerformanceResponse;
import com.edutech.knowledgegap.entity.QuizAttempt;
import com.edutech.knowledgegap.entity.Student;
import com.edutech.knowledgegap.entity.Topic;
import com.edutech.knowledgegap.entity.TopicPerformance;
import com.edutech.knowledgegap.repository.TopicPerformanceRepository;
import com.edutech.knowledgegap.repository.TopicRepository;
import com.edutech.knowledgegap.service.KnowledgeGapDetectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class KnowledgeGapDetectionServiceImpl implements KnowledgeGapDetectionService {

    public static final double HIGH_PRIORITY_THRESHOLD = 40.0;
    public static final double NEEDS_IMPROVEMENT_THRESHOLD = 70.0;
    public static final double GOOD_UNDERSTANDING_THRESHOLD = 85.0;

    private final TopicRepository topicRepository;
    private final TopicPerformanceRepository performanceRepository;
    private final PerformanceThresholds thresholds;

    public KnowledgeGapDetectionServiceImpl(TopicRepository topicRepository,
                                            TopicPerformanceRepository performanceRepository,
                                            PerformanceThresholds thresholds) {
        this.topicRepository = topicRepository;
        this.performanceRepository = performanceRepository;
        this.thresholds = thresholds;
    }

    @Override
    @Transactional
    public List<TopicPerformance> analyzeAndPersistGaps(
            QuizAttempt attempt,
            Student student,
            Map<Long, Integer> topicTotalQuestions,
            Map<Long, Integer> topicCorrectAnswers) {

        List<Topic> attemptedTopics = topicRepository.findAllById(topicTotalQuestions.keySet()).stream()
                .filter(t -> topicTotalQuestions.getOrDefault(t.getId(), 0) > 0)
                .sorted(Comparator.comparing(Topic::getOrderIndex, Comparator.nullsLast(Integer::compareTo)))
                .toList();

        List<TopicPerformance> performances = new ArrayList<>();

        for (Topic topic : attemptedTopics) {
            int attempted = topicTotalQuestions.getOrDefault(topic.getId(), 0);
            int correct = topicCorrectAnswers.getOrDefault(topic.getId(), 0);
            double accuracy = Math.round(((double) correct / attempted) * 1000.0) / 10.0;
            String status = determineGapStatus(attempted, accuracy);

            TopicPerformance performance = new TopicPerformance(
                    attempt,
                    student,
                    topic,
                    attempted,
                    correct,
                    accuracy,
                    status
            );

            performances.add(performanceRepository.save(performance));
        }

        return performances;
    }

    @Override
    public String determineGapStatus(int attempted, double accuracy) {
        if (attempted <= 0) {
            return "NOT_ASSESSED";
        }
        if (attempted < 3) {
            return "NEEDS_FURTHER_ASSESSMENT";
        }
        if (accuracy < thresholds.getHighPriorityThreshold()) {
            return "HIGH_PRIORITY_GAP";
        }
        if (accuracy < thresholds.getNeedsImprovementThreshold()) {
            return "NEEDS_IMPROVEMENT";
        }
        if (accuracy < thresholds.getGoodUnderstandingThreshold()) {
            return "GOOD_UNDERSTANDING";
        }
        return "STRONG_UNDERSTANDING";
    }

    @Override
    public List<TopicPerformanceResponse> formatPerformanceResponses(List<TopicPerformance> performances) {
        return performances.stream().map(p -> {
            String label;
            String color;

            switch (p.getGapStatus()) {
                case "HIGH_PRIORITY_GAP" -> {
                    label = "High-Priority Knowledge Gap";
                    color = "#ef4444"; // red
                }
                case "NEEDS_IMPROVEMENT" -> {
                    label = "Needs Improvement";
                    color = "#f59e0b"; // amber
                }
                case "GOOD_UNDERSTANDING" -> {
                    label = "Good Understanding";
                    color = "#3b82f6"; // blue
                }
                case "STRONG_UNDERSTANDING" -> {
                    label = "Strong Understanding";
                    color = "#10b981"; // emerald
                }
                case "NEEDS_FURTHER_ASSESSMENT" -> {
                    label = "Needs Further Assessment";
                    color = "#eab308"; // amber/yellow
                }
                default -> {
                    label = "Not Assessed";
                    color = "#94a3b8"; // slate
                }
            }

            Long subjectId = p.getTopic().getSubject() != null ? p.getTopic().getSubject().getId() : null;
            String subjectName = p.getTopic().getSubject() != null
                    ? p.getTopic().getSubject().getName()
                    : p.getTopic().getSubjectName();

            return new TopicPerformanceResponse(
                    p.getTopic().getId(),
                    p.getTopic().getName(),
                    subjectId,
                    subjectName,
                    p.getQuestionsAttempted(),
                    p.getCorrectAnswers(),
                    p.getAccuracyPercentage(),
                    p.getGapStatus(),
                    label,
                    color
            );
        }).toList();
    }
}
