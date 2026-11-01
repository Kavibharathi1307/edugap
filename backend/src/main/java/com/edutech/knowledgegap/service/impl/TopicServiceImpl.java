package com.edutech.knowledgegap.service.impl;

import com.edutech.knowledgegap.dto.LearningResourceResponse;
import com.edutech.knowledgegap.dto.TopicResponse;
import com.edutech.knowledgegap.entity.LearningResource;
import com.edutech.knowledgegap.entity.Student;
import com.edutech.knowledgegap.entity.Topic;
import com.edutech.knowledgegap.entity.TopicProgress;
import com.edutech.knowledgegap.exception.ResourceNotFoundException;
import com.edutech.knowledgegap.repository.LearningResourceRepository;
import com.edutech.knowledgegap.repository.SubjectRepository;
import com.edutech.knowledgegap.repository.TopicProgressRepository;
import com.edutech.knowledgegap.repository.TopicRepository;
import com.edutech.knowledgegap.service.TopicService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TopicServiceImpl implements TopicService {

    private final TopicRepository topicRepository;
    private final TopicProgressRepository progressRepository;
    private final LearningResourceRepository resourceRepository;
    private final SubjectRepository subjectRepository;

    public TopicServiceImpl(TopicRepository topicRepository,
                            TopicProgressRepository progressRepository,
                            LearningResourceRepository resourceRepository,
                            SubjectRepository subjectRepository) {
        this.topicRepository = topicRepository;
        this.progressRepository = progressRepository;
        this.resourceRepository = resourceRepository;
        this.subjectRepository = subjectRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopicResponse> getAllTopics(Student student) {
        return getAllTopics(student, null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TopicResponse> getAllTopics(Student student, Long subjectId) {
        List<Topic> topics;
        if (subjectId != null) {
            if (!subjectRepository.existsById(subjectId)) {
                throw new ResourceNotFoundException("Subject not found with id: " + subjectId);
            }
            topics = topicRepository.findBySubjectIdOrderByOrderIndexAsc(subjectId);
        } else {
            topics = topicRepository.findAllByOrderByOrderIndexAsc();
        }

        Map<Long, Boolean> completionMap = student != null
                ? progressRepository.findByStudentId(student.getId()).stream()
                    .collect(Collectors.toMap(tp -> tp.getTopic().getId(), TopicProgress::isCompleted, (a, b) -> a))
                : Map.of();

        return topics.stream()
                .map(t -> mapToResponse(t, completionMap.getOrDefault(t.getId(), false)))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TopicResponse getTopicById(Long topicId, Student student) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id: " + topicId));

        boolean completed = false;
        if (student != null) {
            completed = progressRepository.findByStudentIdAndTopicId(student.getId(), topicId)
                    .map(TopicProgress::isCompleted)
                    .orElse(false);
        }

        return mapToResponse(topic, completed);
    }

    @Override
    public List<LearningResourceResponse> getTopicResources(Long topicId) {
        if (!topicRepository.existsById(topicId)) {
            throw new ResourceNotFoundException("Topic not found with id: " + topicId);
        }
        return resourceRepository.findByTopicId(topicId).stream()
                .map(this::mapResourceToResponse)
                .toList();
    }

    @Override
    @Transactional
    public TopicResponse markTopicProgress(Long topicId, Student student, boolean completed) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id: " + topicId));

        TopicProgress progress = progressRepository.findByStudentIdAndTopicId(student.getId(), topicId)
                .orElseGet(() -> new TopicProgress(student, topic, false));

        progress.setCompleted(completed);
        progressRepository.save(progress);

        return mapToResponse(topic, completed);
    }

    private TopicResponse mapToResponse(Topic topic, boolean isCompleted) {
        List<LearningResourceResponse> resources = resourceRepository.findByTopicId(topic.getId()).stream()
                .map(this::mapResourceToResponse)
                .toList();

        Long subjectId = topic.getSubject() != null ? topic.getSubject().getId() : null;
        String subjectCode = topic.getSubject() != null ? topic.getSubject().getCode() : null;
        String subjectName = topic.getSubject() != null ? topic.getSubject().getName() : topic.getSubjectName();

        return new TopicResponse(
                topic.getId(),
                topic.getName(),
                topic.getDescription(),
                topic.getLearningObjectives(),
                topic.getBeginnerExplanation(),
                topic.getCommonMistakes(),
                topic.getCodeExample(),
                topic.getOrderIndex(),
                subjectId,
                subjectCode,
                subjectName,
                isCompleted,
                resources
        );
    }

    private LearningResourceResponse mapResourceToResponse(LearningResource r) {
        return new LearningResourceResponse(
                r.getId(),
                r.getTitle(),
                r.getContentType(),
                r.getResourceUrl(),
                r.getSummary(),
                r.getDifficultyLevel()
        );
    }
}
