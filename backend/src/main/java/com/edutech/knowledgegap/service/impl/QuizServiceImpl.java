package com.edutech.knowledgegap.service.impl;

import com.edutech.knowledgegap.dto.*;
import com.edutech.knowledgegap.entity.*;
import com.edutech.knowledgegap.exception.BadRequestException;
import com.edutech.knowledgegap.exception.ResourceNotFoundException;
import com.edutech.knowledgegap.repository.*;
import com.edutech.knowledgegap.service.KnowledgeGapDetectionService;
import com.edutech.knowledgegap.service.PersonalizedRecommendationService;
import com.edutech.knowledgegap.service.QuizService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuizServiceImpl implements QuizService {

    private final QuestionRepository questionRepository;
    private final QuestionOptionRepository optionRepository;
    private final QuizAttemptRepository attemptRepository;
    private final StudentAnswerRepository answerRepository;
    private final TopicPerformanceRepository performanceRepository;
    private final LearningRecommendationRepository recommendationRepository;
    private final SubjectRepository subjectRepository;
    private final KnowledgeGapDetectionService gapDetectionService;
    private final PersonalizedRecommendationService recommendationService;

    public QuizServiceImpl(QuestionRepository questionRepository,
                           QuestionOptionRepository optionRepository,
                           QuizAttemptRepository attemptRepository,
                           StudentAnswerRepository answerRepository,
                           TopicPerformanceRepository performanceRepository,
                           LearningRecommendationRepository recommendationRepository,
                           SubjectRepository subjectRepository,
                           KnowledgeGapDetectionService gapDetectionService,
                           PersonalizedRecommendationService recommendationService) {
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
        this.attemptRepository = attemptRepository;
        this.answerRepository = answerRepository;
        this.performanceRepository = performanceRepository;
        this.recommendationRepository = recommendationRepository;
        this.subjectRepository = subjectRepository;
        this.gapDetectionService = gapDetectionService;
        this.recommendationService = recommendationService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionResponse> generateQuizQuestions(Long topicId, Integer limit) {
        return generateQuizQuestions(topicId, null, limit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuestionResponse> generateQuizQuestions(Long topicId, Long subjectId, Integer limit) {
        Subject selectedSubject = subjectId != null
                ? subjectRepository.findById(subjectId)
                        .orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + subjectId))
                : null;

        List<Question> questions;
        if (topicId != null && topicId > 0) {
            questions = questionRepository.findByTopicIdWithTopic(topicId);
            if (questions.isEmpty()) {
                throw new ResourceNotFoundException("No questions available for topic id: " + topicId);
            }
            if (selectedSubject != null) {
                boolean belongs = questions.stream()
                        .allMatch(q -> q.getTopic().getSubject() != null
                                && selectedSubject.getId().equals(q.getTopic().getSubject().getId()));
                if (!belongs) {
                    throw new BadRequestException("The selected topic does not belong to the selected subject");
                }
            }
        } else if (selectedSubject != null) {
            questions = questionRepository.findBySubjectIdWithTopic(selectedSubject.getId());
        } else {
            questions = questionRepository.findRandomQuestions();
        }

        List<Question> shuffled = new ArrayList<>(questions);
        Collections.shuffle(shuffled);
        int max = (limit != null && limit > 0) ? Math.min(limit, shuffled.size()) : shuffled.size();
        shuffled = shuffled.subList(0, max);

        return shuffled.stream().map(q -> {
            Topic topic = q.getTopic();
            Subject qSubject = topic.getSubject() != null ? topic.getSubject() : selectedSubject;
            List<QuestionOptionResponse> options = optionRepository.findByQuestionId(q.getId()).stream()
                    .map(o -> new QuestionOptionResponse(o.getId(), o.getOptionKey(), o.getOptionText()))
                    .toList();

            return new QuestionResponse(
                    q.getId(),
                    topic.getId(),
                    topic.getName(),
                    qSubject != null ? qSubject.getId() : null,
                    qSubject != null ? qSubject.getName() : topic.getSubjectName(),
                    q.getQuestionText(),
                    q.getDifficultyLevel(),
                    options
            );
        }).toList();
    }

    @Override
    @Transactional
    public QuizResultResponse submitQuiz(QuizSubmissionRequest request, Student student) {
        if (request.getAnswers() == null || request.getAnswers().isEmpty()) {
            throw new BadRequestException("Submission must contain at least one question answer");
        }

        List<QuestionAnswerSubmission> submissions = request.getAnswers();
        Set<Long> seenQuestionIds = new HashSet<>();
        List<Question> questions = new ArrayList<>(submissions.size());
        Subject resolvedSubject = null;

        for (QuestionAnswerSubmission sub : submissions) {
            if (sub.getQuestionId() == null) {
                throw new BadRequestException("Every answer must include a questionId");
            }
            if (!seenQuestionIds.add(sub.getQuestionId())) {
                throw new BadRequestException("Duplicate question id in submission: " + sub.getQuestionId());
            }

            Question question = questionRepository.findById(sub.getQuestionId())
                    .orElseThrow(() -> new ResourceNotFoundException("Question not found with id: " + sub.getQuestionId()));

            Subject questionSubject = resolveSubject(question);
            if (questionSubject == null) {
                throw new BadRequestException("Question " + question.getId() + " is not linked to a subject");
            }
            if (resolvedSubject == null) {
                resolvedSubject = questionSubject;
            } else if (!resolvedSubject.getId().equals(questionSubject.getId())) {
                throw new BadRequestException("A single assessment cannot mix questions from different subjects");
            }

            if (sub.getSelectedOptionKey() != null && !sub.getSelectedOptionKey().isBlank()) {
                optionRepository.findByQuestionIdAndOptionKey(question.getId(), sub.getSelectedOptionKey().trim())
                        .orElseThrow(() -> new BadRequestException(
                                "Invalid option '" + sub.getSelectedOptionKey() + "' for question " + question.getId()));
            }

            questions.add(question);
        }

        if (request.getSubjectId() != null && !request.getSubjectId().equals(resolvedSubject.getId())) {
            throw new BadRequestException("Submitted questions do not belong to the requested subject");
        }

        int totalQuestions = questions.size();
        int correctCount = 0;
        Map<Long, Integer> topicTotalMap = new HashMap<>();
        Map<Long, Integer> topicCorrectMap = new HashMap<>();
        List<AnswerReviewResponse> answerReviews = new ArrayList<>();
        List<StudentAnswer> studentAnswersToSave = new ArrayList<>();

        QuizAttempt attempt = new QuizAttempt(
                student,
                resolvedSubject,
                totalQuestions,
                0,
                0.0
        );
        attempt = attemptRepository.save(attempt);

        for (int i = 0; i < questions.size(); i++) {
            Question question = questions.get(i);
            String selectedKey = submissions.get(i).getSelectedOptionKey();
            if (selectedKey != null) {
                selectedKey = selectedKey.trim();
            }

            Long topicId = question.getTopic().getId();
            topicTotalMap.put(topicId, topicTotalMap.getOrDefault(topicId, 0) + 1);

            QuestionOption correctOption = optionRepository.findByQuestionIdAndIsCorrectTrue(question.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Correct answer option missing for question: " + question.getId()));

            boolean isCorrect = selectedKey != null
                    && !selectedKey.isBlank()
                    && selectedKey.equalsIgnoreCase(correctOption.getOptionKey().trim());

            if (isCorrect) {
                correctCount++;
                topicCorrectMap.put(topicId, topicCorrectMap.getOrDefault(topicId, 0) + 1);
            }

            StudentAnswer studentAnswer = new StudentAnswer(
                    attempt,
                    question,
                    selectedKey == null || selectedKey.isBlank() ? null : selectedKey,
                    isCorrect
            );
            studentAnswersToSave.add(studentAnswer);

            answerReviews.add(new AnswerReviewResponse(
                    question.getId(),
                    question.getQuestionText(),
                    question.getTopic().getName(),
                    selectedKey == null || selectedKey.isBlank() ? null : selectedKey,
                    correctOption.getOptionKey(),
                    isCorrect,
                    question.getExplanation()
            ));
        }

        answerRepository.saveAll(studentAnswersToSave);

        double scorePercentage = Math.round(((double) correctCount / totalQuestions) * 1000.0) / 10.0;
        attempt.setCorrectAnswers(correctCount);
        attempt.setScorePercentage(scorePercentage);
        attempt = attemptRepository.save(attempt);

        List<TopicPerformance> performances = gapDetectionService.analyzeAndPersistGaps(
                attempt, student, topicTotalMap, topicCorrectMap
        );

        List<LearningRecommendationResponse> recommendations = recommendationService.generateRecommendations(
                attempt, student, performances
        );

        List<TopicPerformanceResponse> performanceResponses = gapDetectionService.formatPerformanceResponses(performances);

        return new QuizResultResponse(
                attempt.getId(),
                resolvedSubject.getId(),
                resolvedSubject.getName(),
                totalQuestions,
                correctCount,
                scorePercentage,
                attempt.getCompletedAt(),
                performanceResponses,
                answerReviews,
                recommendations
        );
    }

    @Override
    @Transactional(readOnly = true)
    public QuizResultResponse getAttemptResult(Long attemptId, Student student) {
        QuizAttempt attempt = attemptRepository.findByIdAndStudentId(attemptId, student.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz attempt not found or unauthorized"));

        List<TopicPerformance> performances = performanceRepository.findByQuizAttemptId(attempt.getId());
        List<TopicPerformanceResponse> perfResponses = gapDetectionService.formatPerformanceResponses(performances);

        List<StudentAnswer> answers = answerRepository.findByQuizAttemptId(attempt.getId());
        List<AnswerReviewResponse> answerReviews = answers.stream().map(a -> {
            Question q = a.getQuestion();
            QuestionOption correctOpt = optionRepository.findByQuestionIdAndIsCorrectTrue(q.getId()).orElse(null);
            return new AnswerReviewResponse(
                    q.getId(),
                    q.getQuestionText(),
                    q.getTopic().getName(),
                    a.getSelectedOptionKey(),
                    correctOpt != null ? correctOpt.getOptionKey() : "",
                    a.isCorrect(),
                    q.getExplanation()
            );
        }).toList();

        List<LearningRecommendationResponse> recommendations = recommendationRepository.findByStudentIdOrderByCreatedAtDesc(student.getId())
                .stream()
                .filter(r -> r.getQuizAttempt() != null && r.getQuizAttempt().getId().equals(attempt.getId()))
                .map(this::mapRecommendation)
                .toList();

        return new QuizResultResponse(
                attempt.getId(),
                attempt.getSubject() != null ? attempt.getSubject().getId() : null,
                attempt.getSubject() != null ? attempt.getSubject().getName() : attempt.getSubjectName(),
                attempt.getTotalQuestions(),
                attempt.getCorrectAnswers(),
                attempt.getScorePercentage(),
                attempt.getCompletedAt(),
                perfResponses,
                answerReviews,
                recommendations
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<Map<String, Object>> getAttemptHistory(Student student) {
        List<QuizAttempt> attempts = attemptRepository.findByStudentIdOrderByCompletedAtDesc(student.getId());

        return attempts.stream().map(a -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("attemptId", a.getId());
            map.put("subjectId", a.getSubject() != null ? a.getSubject().getId() : null);
            map.put("subjectName", resolvedSubjectName(a));
            map.put("totalQuestions", a.getTotalQuestions());
            map.put("correctAnswers", a.getCorrectAnswers());
            map.put("scorePercentage", a.getScorePercentage());
            map.put("completedAt", a.getCompletedAt());
            return map;
        }).toList();
    }

    private Subject resolveSubject(Question question) {
        Topic topic = question.getTopic();
        if (topic == null) {
            return null;
        }
        if (topic.getSubject() != null) {
            return topic.getSubject();
        }
        if (topic.getSubjectName() == null) {
            return null;
        }
        return subjectRepository.findByNameIgnoreCase(topic.getSubjectName())
                .or(() -> subjectRepository.findByCodeIgnoreCase(topic.getSubjectName()))
                .orElseGet(() -> {
                    if (Set.of("Core Java", "Java", "Java Programming").contains(topic.getSubjectName())) {
                        return subjectRepository.findByCodeIgnoreCase("JAVA").orElse(null);
                    }
                    return null;
                });
    }

    private String resolvedSubjectName(QuizAttempt attempt) {
        return attempt.getSubject() != null ? attempt.getSubject().getName() : attempt.getSubjectName();
    }

    private LearningRecommendationResponse mapRecommendation(LearningRecommendation r) {
        LearningResource resource = r.getResource();
        Subject subject = r.getTopic().getSubject();
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
