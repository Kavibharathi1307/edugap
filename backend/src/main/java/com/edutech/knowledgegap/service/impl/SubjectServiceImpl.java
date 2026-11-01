package com.edutech.knowledgegap.service.impl;

import com.edutech.knowledgegap.dto.SubjectDetailResponse;
import com.edutech.knowledgegap.dto.SubjectProgressResponse;
import com.edutech.knowledgegap.dto.SubjectResponse;
import com.edutech.knowledgegap.dto.TopicSummaryResponse;
import com.edutech.knowledgegap.entity.*;
import com.edutech.knowledgegap.exception.ResourceNotFoundException;
import com.edutech.knowledgegap.repository.*;
import com.edutech.knowledgegap.service.SubjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;
    private final TopicRepository topicRepository;
    private final TopicPerformanceRepository performanceRepository;
    private final TopicProgressRepository progressRepository;
    private final QuizAttemptRepository attemptRepository;
    private final QuestionRepository questionRepository;

    public SubjectServiceImpl(SubjectRepository subjectRepository,
                              TopicRepository topicRepository,
                              TopicPerformanceRepository performanceRepository,
                              TopicProgressRepository progressRepository,
                              QuizAttemptRepository attemptRepository,
                              QuestionRepository questionRepository) {
        this.subjectRepository = subjectRepository;
        this.topicRepository = topicRepository;
        this.performanceRepository = performanceRepository;
        this.progressRepository = progressRepository;
        this.attemptRepository = attemptRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubjectResponse> getSubjects(Student student) {
        List<Subject> subjects = subjectRepository.findByActiveTrueOrderByOrderIndexAsc();
        Map<Long, TopicPerformance> latestByTopic = latestPerformancesByTopic(student.getId());
        List<QuizAttempt> allAttempts = attemptRepository.findByStudentIdOrderByCompletedAtDesc(student.getId());

        return subjects.stream()
                .map(subject -> {
                    List<Topic> topics = topicRepository.findBySubjectIdOrderByOrderIndexAsc(subject.getId());
                    long assessed = topics.stream()
                            .filter(t -> isAssessed(latestByTopic.get(t.getId())))
                            .count();
                    List<QuizAttempt> attempts = attemptsForSubject(allAttempts, subject);

                    double average = attempts.stream()
                            .mapToDouble(QuizAttempt::getScorePercentage)
                            .average()
                            .orElse(0.0);
                    Double latest = attempts.isEmpty() ? null : attempts.get(0).getScorePercentage();
                    LocalDateTime lastAttemptAt = attempts.isEmpty() ? null : attempts.get(0).getCompletedAt();

                    return new SubjectResponse(
                            subject.getId(),
                            subject.getCode(),
                            subject.getName(),
                            subject.getDescription(),
                            subject.getIcon(),
                            subject.getOrderIndex(),
                            topics.size(),
                            assessed,
                            attempts.size(),
                            round(average),
                            latest,
                            lastAttemptAt
                    );
                })
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SubjectDetailResponse getSubjectDetail(Long subjectId, Student student) {
        Subject subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + subjectId));

        List<Topic> topics = topicRepository.findBySubjectIdOrderByOrderIndexAsc(subject.getId());
        Map<Long, TopicPerformance> latestByTopic = latestPerformancesByTopic(student.getId());
        Map<Long, Boolean> completion = progressRepository.findByStudentId(student.getId()).stream()
                .collect(Collectors.toMap(tp -> tp.getTopic().getId(), TopicProgress::isCompleted, (a, b) -> a));

        List<TopicSummaryResponse> topicSummaries = topics.stream()
                .map(topic -> mapTopicSummary(topic, latestByTopic.get(topic.getId()),
                        completion.getOrDefault(topic.getId(), false)))
                .toList();

        long assessed = topicSummaries.stream().filter(TopicSummaryResponse::isAssessed).count();
        List<QuizAttempt> subjectAttempts = attemptsForSubject(
                attemptRepository.findByStudentIdOrderByCompletedAtDesc(student.getId()), subject);

        double average = subjectAttempts.stream()
                .mapToDouble(QuizAttempt::getScorePercentage)
                .average()
                .orElse(0.0);

        List<Map<String, Object>> recentAttempts = subjectAttempts.stream().limit(5).map(a -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("attemptId", a.getId());
            map.put("totalQuestions", a.getTotalQuestions());
            map.put("correctAnswers", a.getCorrectAnswers());
            map.put("scorePercentage", a.getScorePercentage());
            map.put("completedAt", a.getCompletedAt());
            return map;
        }).toList();

        return new SubjectDetailResponse(
                subject.getId(),
                subject.getCode(),
                subject.getName(),
                subject.getDescription(),
                subject.getIcon(),
                subject.getOrderIndex(),
                topics.size(),
                assessed,
                subjectAttempts.size(),
                round(average),
                subjectAttempts.isEmpty() ? null : subjectAttempts.get(0).getScorePercentage(),
                subjectAttempts.isEmpty() ? null : subjectAttempts.get(0).getCompletedAt(),
                topicSummaries,
                recentAttempts
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubjectProgressResponse> getSubjectProgress(Student student) {
        List<Subject> subjects = subjectRepository.findByActiveTrueOrderByOrderIndexAsc();
        Map<Long, TopicPerformance> latestByTopic = latestPerformancesByTopic(student.getId());
        List<QuizAttempt> allAttempts = attemptRepository.findByStudentIdOrderByCompletedAtDesc(student.getId());

        return subjects.stream().map(subject -> {
            List<Topic> topics = topicRepository.findBySubjectIdOrderByOrderIndexAsc(subject.getId());
            List<QuizAttempt> attempts = attemptsForSubject(allAttempts, subject);

            List<TopicPerformance> assessedPerformances = topics.stream()
                    .map(t -> latestByTopic.get(t.getId()))
                    .filter(this::isAssessed)
                    .toList();

            double overallProgress = assessedPerformances.stream()
                    .mapToDouble(TopicPerformance::getAccuracyPercentage)
                    .average()
                    .orElse(0.0);

            double average = attempts.stream()
                    .mapToDouble(QuizAttempt::getScorePercentage)
                    .average()
                    .orElse(0.0);

            Double latest = attempts.isEmpty() ? null : attempts.get(0).getScorePercentage();
            Double previous = attempts.size() < 2 ? null : attempts.get(1).getScorePercentage();
            Double scoreChange = latest != null && previous != null
                    ? round(latest - previous)
                    : null;
            Double improvement = attempts.size() < 2
                    ? null
                    : round(attempts.get(0).getScorePercentage()
                        - attempts.get(attempts.size() - 1).getScorePercentage());

            return new SubjectProgressResponse(
                    subject.getId(),
                    subject.getCode(),
                    subject.getName(),
                    topics.size(),
                    assessedPerformances.size(),
                    attempts.size(),
                    round(average),
                    latest,
                    previous,
                    scoreChange,
                    improvement,
                    round(overallProgress),
                    attempts.isEmpty() ? null : attempts.get(0).getCompletedAt()
            );
        }).toList();
    }

    private TopicSummaryResponse mapTopicSummary(Topic topic, TopicPerformance performance, boolean completed) {
        boolean assessed = isAssessed(performance);
        String status = assessed ? performance.getGapStatus() : "NOT_ASSESSED";
        GapBadge badge = badgeFor(status);

        return new TopicSummaryResponse(
                topic.getId(),
                topic.getName(),
                topic.getDescription(),
                topic.getOrderIndex(),
                topic.getSubject() != null ? topic.getSubject().getId() : null,
                topic.getSubject() != null ? topic.getSubject().getName() : topic.getSubjectName(),
                questionRepository.countByTopicId(topic.getId()),
                assessed,
                completed,
                assessed ? performance.getQuestionsAttempted() : 0,
                assessed ? performance.getCorrectAnswers() : 0,
                assessed ? performance.getAccuracyPercentage() : 0.0,
                status,
                badge.label,
                badge.color
        );
    }

    private Map<Long, TopicPerformance> latestPerformancesByTopic(Long studentId) {
        return performanceRepository.findLatestPerformancesByStudentId(studentId).stream()
                .collect(Collectors.toMap(
                        p -> p.getTopic().getId(),
                        Function.identity(),
                        (a, b) -> a
                ));
    }

    private boolean isAssessed(TopicPerformance performance) {
        return performance != null
                && performance.getQuestionsAttempted() > 0
                && !"NOT_ASSESSED".equals(performance.getGapStatus());
    }

    private List<QuizAttempt> attemptsForSubject(List<QuizAttempt> attempts, Subject subject) {
        return attempts.stream()
                .filter(a -> belongsToSubject(a, subject))
                .toList();
    }

    private boolean belongsToSubject(QuizAttempt attempt, Subject subject) {
        if (attempt.getSubject() != null) {
            return attempt.getSubject().getId().equals(subject.getId());
        }
        if (attempt.getSubjectName() == null) {
            return false;
        }
        String name = attempt.getSubjectName();
        return name.equalsIgnoreCase(subject.getName())
                || name.equalsIgnoreCase(subject.getCode())
                || ("JAVA".equalsIgnoreCase(subject.getCode())
                    && Set.of("Core Java", "Java", "Java Programming").contains(name));
    }

    private static double round(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    private record GapBadge(String label, String color) {}

    private GapBadge badgeFor(String status) {
        return switch (status) {
            case "HIGH_PRIORITY_GAP" -> new GapBadge("High-Priority Knowledge Gap", "#ef4444");
            case "NEEDS_IMPROVEMENT" -> new GapBadge("Needs Improvement", "#f59e0b");
            case "GOOD_UNDERSTANDING" -> new GapBadge("Good Understanding", "#3b82f6");
            case "STRONG_UNDERSTANDING" -> new GapBadge("Strong Understanding", "#10b981");
            default -> new GapBadge("Not assessed", "#94a3b8");
        };
    }
}
