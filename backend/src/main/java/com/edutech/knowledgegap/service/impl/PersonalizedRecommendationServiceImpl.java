package com.edutech.knowledgegap.service.impl;

import com.edutech.knowledgegap.config.PerformanceThresholds;
import com.edutech.knowledgegap.dto.LearningRecommendationResponse;
import com.edutech.knowledgegap.entity.LearningRecommendation;
import com.edutech.knowledgegap.entity.LearningResource;
import com.edutech.knowledgegap.entity.QuizAttempt;
import com.edutech.knowledgegap.entity.Student;
import com.edutech.knowledgegap.entity.Topic;
import com.edutech.knowledgegap.entity.TopicPerformance;
import com.edutech.knowledgegap.exception.ResourceNotFoundException;
import com.edutech.knowledgegap.repository.LearningRecommendationRepository;
import com.edutech.knowledgegap.repository.LearningResourceRepository;
import com.edutech.knowledgegap.repository.TopicPerformanceRepository;
import com.edutech.knowledgegap.service.PersonalizedRecommendationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonalizedRecommendationServiceImpl implements PersonalizedRecommendationService {

    private final LearningRecommendationRepository recommendationRepository;
    private final LearningResourceRepository resourceRepository;
    private final TopicPerformanceRepository performanceRepository;
    private final PerformanceThresholds thresholds;

    public PersonalizedRecommendationServiceImpl(LearningRecommendationRepository recommendationRepository,
                                                LearningResourceRepository resourceRepository,
                                                TopicPerformanceRepository performanceRepository,
                                                PerformanceThresholds thresholds) {
        this.recommendationRepository = recommendationRepository;
        this.resourceRepository = resourceRepository;
        this.performanceRepository = performanceRepository;
        this.thresholds = thresholds;
    }

    @Override
    @Transactional
    public List<LearningRecommendationResponse> generateRecommendations(
            QuizAttempt attempt,
            Student student,
            List<TopicPerformance> performances) {

        List<LearningRecommendationResponse> responses = new ArrayList<>();

        for (TopicPerformance currentPerf : performances) {
            Topic topic = currentPerf.getTopic();
            String status = currentPerf.getGapStatus();
            double accuracy = currentPerf.getAccuracyPercentage();

            if ("NOT_ASSESSED".equals(status) || "STRONG_UNDERSTANDING".equals(status)) {
                continue; // Only generate remedial recommendations for gaps or moderate understanding
            }

            // Look up previous attempt performance for this topic to check progress delta
            List<TopicPerformance> history = performanceRepository
                    .findByStudentIdAndTopicIdOrderByRecordedAtDesc(student.getId(), topic.getId());

            String deltaNote = "";
            // If at least two records exist (including the one just saved)
            if (history.size() >= 2) {
                TopicPerformance previousPerf = history.get(1); // 0 is current, 1 is immediately preceding
                double delta = Math.round((accuracy - previousPerf.getAccuracyPercentage()) * 10.0) / 10.0;
                if (delta > 0) {
                    deltaNote = " (You showed positive growth: +" + delta + "% vs previous attempt).";
                } else if (delta < 0) {
                    deltaNote = " (Noticeable decline: " + delta + "% vs previous attempt. Review recommended).";
                }
            }

            LearningResource resource = pickResource(topic);
            String lower = format(thresholds.getHighPriorityThreshold());
            String upper = format(thresholds.getGoodUnderstandingThreshold());

            if ("HIGH_PRIORITY_GAP".equals(status)) {
                // Recommendation 1: Study Topic Notes
                String text1 = "Study the comprehensive notes, architecture overview, and examples for " + topic.getName() + ".";
                String reason1 = "Your accuracy was " + accuracy + "%, which is below the " + lower + "% mastery threshold." + deltaNote;
                LearningRecommendation rec1 = new LearningRecommendation(
                        student, topic, attempt, resource, text1, "STUDY_TOPIC", "HIGH", reason1
                );
                recommendationRepository.save(rec1);
                responses.add(mapToResponse(rec1));

                // Recommendation 2: Follow-up Quiz
                String text2 = "Take a focused follow-up assessment on " + topic.getName() + " to test concept retention.";
                String reason2 = "Frequent retrieval practice is proven to turn high-priority gaps into solid memory.";
                LearningRecommendation rec2 = new LearningRecommendation(
                        student, topic, attempt, resource, text2, "PRACTICE_QUIZ", "HIGH", reason2
                );
                recommendationRepository.save(rec2);
                responses.add(mapToResponse(rec2));

            } else if ("NEEDS_IMPROVEMENT".equals(status)) {
                String text = "Review the common mistakes and key patterns for " + topic.getName() + " using the linked resource.";
                String reason = "Your score of " + accuracy + "% reflects partial understanding (" + lower + "-" + format(thresholds.getNeedsImprovementThreshold()) + "%). Eliminating common pitfalls will elevate your score." + deltaNote;
                LearningRecommendation rec = new LearningRecommendation(
                        student, topic, attempt, resource, text, "CODE_EXERCISE", "MEDIUM", reason
                );
                recommendationRepository.save(rec);
                responses.add(mapToResponse(rec));

            } else if ("GOOD_UNDERSTANDING".equals(status)) {
                String text = "Challenge yourself with advanced questions and implementation nuances in " + topic.getName() + ".";
                String reason = "Solid performance (" + accuracy + "%). A quick review will achieve total mastery (>=" + upper + "%).";
                LearningRecommendation rec = new LearningRecommendation(
                        student, topic, attempt, resource, text, "PRACTICE_QUIZ", "LOW", reason
                );
                recommendationRepository.save(rec);
                responses.add(mapToResponse(rec));
            }
        }

        return responses;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LearningRecommendationResponse> getActiveRecommendations(Student student) {
        return recommendationRepository.findByStudentIdAndIsCompletedFalseOrderByPriorityAsc(student.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public LearningRecommendationResponse markAsCompleted(Long recommendationId, Student student) {
        LearningRecommendation rec = recommendationRepository.findByIdAndStudentId(recommendationId, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Recommendation not found or unauthorized"));

        rec.setCompleted(true);
        recommendationRepository.save(rec);

        return mapToResponse(rec);
    }

    private LearningResource pickResource(Topic topic) {
        List<LearningResource> resources = resourceRepository.findByTopicId(topic.getId());
        return resources.isEmpty() ? null : resources.get(0);
    }

    private String format(double value) {
        return value == Math.rint(value) ? String.valueOf((int) value) : String.valueOf(value);
    }

    private LearningRecommendationResponse mapToResponse(LearningRecommendation r) {
        LearningResource resource = r.getResource();
        var subject = r.getTopic().getSubject();
        return new LearningRecommendationResponse(
                r.getId(),
                r.getTopic().getId(),
                r.getTopic().getName(),
                subject != null ? subject.getId() : null,
                subject != null ? subject.getName() : r.getTopic().getSubjectName(),
                resource != null ? resource.getId() : null,
                resource != null ? resource.getTitle() : null,
                resource != null ? resource.getResourceUrl() : null,
                resource != null ? resource.getContentType() : null,
                r.getRecommendationText(),
                r.getActionType(),
                r.getPriority(),
                r.getReason(),
                r.isCompleted(),
                r.getCreatedAt()
        );
    }
}
