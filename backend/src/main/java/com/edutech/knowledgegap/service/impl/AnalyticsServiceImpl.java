package com.edutech.knowledgegap.service.impl;

import com.edutech.knowledgegap.dto.DashboardStatsResponse;
import com.edutech.knowledgegap.dto.ProgressHistoryResponse;
import com.edutech.knowledgegap.dto.SubjectProgressResponse;
import com.edutech.knowledgegap.dto.TopicPerformanceResponse;
import com.edutech.knowledgegap.entity.QuizAttempt;
import com.edutech.knowledgegap.entity.Student;
import com.edutech.knowledgegap.entity.Topic;
import com.edutech.knowledgegap.entity.TopicPerformance;
import com.edutech.knowledgegap.repository.*;
import com.edutech.knowledgegap.service.AnalyticsService;
import com.edutech.knowledgegap.service.KnowledgeGapDetectionService;
import com.edutech.knowledgegap.service.SubjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final QuizAttemptRepository attemptRepository;
    private final TopicRepository topicRepository;
    private final TopicPerformanceRepository performanceRepository;
    private final TopicProgressRepository progressRepository;
    private final LearningRecommendationRepository recommendationRepository;
    private final KnowledgeGapDetectionService gapDetectionService;
    private final SubjectService subjectService;

    public AnalyticsServiceImpl(QuizAttemptRepository attemptRepository,
                                TopicRepository topicRepository,
                                TopicPerformanceRepository performanceRepository,
                                TopicProgressRepository progressRepository,
                                LearningRecommendationRepository recommendationRepository,
                                KnowledgeGapDetectionService gapDetectionService,
                                SubjectService subjectService) {
        this.attemptRepository = attemptRepository;
        this.topicRepository = topicRepository;
        this.performanceRepository = performanceRepository;
        this.progressRepository = progressRepository;
        this.recommendationRepository = recommendationRepository;
        this.gapDetectionService = gapDetectionService;
        this.subjectService = subjectService;
    }

    @Override
    @Transactional(readOnly = true)
    public DashboardStatsResponse getDashboardStats(Student student) {
        List<QuizAttempt> attempts = attemptRepository.findByStudentIdOrderByCompletedAtDesc(student.getId());
        long totalAttempts = attempts.size();
        double avgScore = attempts.isEmpty() ? 0.0 :
                attempts.stream().mapToDouble(QuizAttempt::getScorePercentage).average().orElse(0.0);

        long completedTopics = progressRepository.countByStudentIdAndIsCompletedTrue(student.getId());
        long totalTopics = topicRepository.count();
        long activeRecs = recommendationRepository.findByStudentIdAndIsCompletedFalseOrderByPriorityAsc(student.getId()).size();

        // Get latest topic mastery
        List<TopicPerformanceResponse> allTopicsPerformance = getTopicMasteryOverview(student);

        List<TopicPerformanceResponse> assessedTopics = allTopicsPerformance.stream()
                .filter(t -> !"NOT_ASSESSED".equals(t.getGapStatus()))
                .toList();

        List<TopicPerformanceResponse> strongestTopics = assessedTopics.stream()
                .sorted(Comparator.comparingDouble(TopicPerformanceResponse::getAccuracyPercentage).reversed())
                .limit(3)
                .toList();

        List<TopicPerformanceResponse> weakestTopics = assessedTopics.stream()
                .filter(t -> "HIGH_PRIORITY_GAP".equals(t.getGapStatus()) || "NEEDS_IMPROVEMENT".equals(t.getGapStatus()))
                .sorted(Comparator.comparingDouble(TopicPerformanceResponse::getAccuracyPercentage))
                .toList();

        List<TopicPerformanceResponse> unassessedTopics = allTopicsPerformance.stream()
                .filter(t -> "NOT_ASSESSED".equals(t.getGapStatus()))
                .toList();

        List<Map<String, Object>> recentAttempts = attempts.stream().limit(5).map(a -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("attemptId", a.getId());
            map.put("subjectId", a.getSubject() != null ? a.getSubject().getId() : null);
            map.put("subjectName", a.getSubject() != null ? a.getSubject().getName() : a.getSubjectName());
            map.put("totalQuestions", a.getTotalQuestions());
            map.put("correctAnswers", a.getCorrectAnswers());
            map.put("scorePercentage", a.getScorePercentage());
            map.put("completedAt", a.getCompletedAt());
            return map;
        }).toList();

        List<SubjectProgressResponse> subjectProgress = subjectService.getSubjectProgress(student);
        long totalSubjects = subjectProgress.size();
        long assessedSubjects = subjectProgress.stream()
                .filter(s -> s.getLatestScore() != null)
                .count();
        double overallProgress = subjectProgress.stream()
                .filter(s -> s.getLatestScore() != null)
                .mapToDouble(SubjectProgressResponse::getOverallProgressPercentage)
                .average()
                .orElse(0.0);
        Double improvement = attempts.size() < 2
                ? null
                : round(attempts.get(0).getScorePercentage()
                    - attempts.get(attempts.size() - 1).getScorePercentage());

        return new DashboardStatsResponse(
                totalAttempts,
                Math.round(avgScore * 10.0) / 10.0,
                completedTopics,
                totalTopics,
                activeRecs,
                strongestTopics,
                weakestTopics,
                unassessedTopics,
                allTopicsPerformance,
                recentAttempts,
                subjectProgress,
                assessedSubjects,
                totalSubjects,
                round(overallProgress),
                improvement
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopicPerformanceResponse> getTopicMasteryOverview(Student student) {
        List<Topic> allTopics = topicRepository.findAllByOrderByOrderIndexAsc();
        List<TopicPerformance> latestPerformances = performanceRepository.findLatestPerformancesByStudentId(student.getId());

        Map<Long, TopicPerformance> performanceByTopicId = latestPerformances.stream()
                .collect(Collectors.toMap(p -> p.getTopic().getId(), p -> p, (a, b) -> a));

        List<TopicPerformance> synthesizedList = new ArrayList<>();
        for (Topic t : allTopics) {
            if (performanceByTopicId.containsKey(t.getId())) {
                synthesizedList.add(performanceByTopicId.get(t.getId()));
            } else {
                TopicPerformance unassessed = new TopicPerformance(
                        null, student, t, 0, 0, 0.0, "NOT_ASSESSED"
                );
                synthesizedList.add(unassessed);
            }
        }

        return gapDetectionService.formatPerformanceResponses(synthesizedList);
    }

    @Override
    @Transactional(readOnly = true)
    public ProgressHistoryResponse getProgressHistory(Student student) {
        List<QuizAttempt> attemptsDesc = attemptRepository.findByStudentIdOrderByCompletedAtDesc(student.getId());
        List<QuizAttempt> attemptsAsc = new ArrayList<>(attemptsDesc);
        Collections.reverse(attemptsAsc);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd");

        List<String> labels = new ArrayList<>();
        List<Double> overallScores = new ArrayList<>();
        Map<String, List<Double>> topicTrends = new HashMap<>();
        Map<String, List<Double>> subjectTrends = new LinkedHashMap<>();

        for (int i = 0; i < attemptsAsc.size(); i++) {
            QuizAttempt a = attemptsAsc.get(i);
            labels.add("Quiz #" + (i + 1) + " (" + a.getCompletedAt().format(formatter) + ")");
            overallScores.add(a.getScorePercentage());

            String subjectName = a.getSubject() != null ? a.getSubject().getName() : a.getSubjectName();
            if (subjectName != null) {
                subjectTrends.putIfAbsent(subjectName, new ArrayList<>());
                subjectTrends.get(subjectName).add(a.getScorePercentage());
            }

            List<TopicPerformance> perfs = performanceRepository.findByQuizAttemptId(a.getId());
            for (TopicPerformance tp : perfs) {
                String topicName = tp.getTopic().getName();
                topicTrends.putIfAbsent(topicName, new ArrayList<>());
                topicTrends.get(topicName).add(tp.getAccuracyPercentage());
            }
        }

        Double improvement = attemptsAsc.size() < 2
                ? null
                : round(attemptsAsc.get(attemptsAsc.size() - 1).getScorePercentage()
                    - attemptsAsc.get(0).getScorePercentage());

        return new ProgressHistoryResponse(labels, overallScores, topicTrends, subjectTrends, improvement);
    }

    private static double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }
}
