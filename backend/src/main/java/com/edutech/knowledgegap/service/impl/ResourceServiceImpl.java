package com.edutech.knowledgegap.service.impl;

import com.edutech.knowledgegap.dto.LearningResourceResponse;
import com.edutech.knowledgegap.entity.LearningResource;
import com.edutech.knowledgegap.entity.Topic;
import com.edutech.knowledgegap.exception.ResourceNotFoundException;
import com.edutech.knowledgegap.repository.LearningResourceRepository;
import com.edutech.knowledgegap.service.ResourceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {

    private final LearningResourceRepository resourceRepository;

    public ResourceServiceImpl(LearningResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<LearningResourceResponse> getAllResources() {
        return resourceRepository.findAllWithTopic().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public LearningResourceResponse getResourceById(Long resourceId) {
        LearningResource resource = resourceRepository.findByIdWithTopic(resourceId)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id: " + resourceId));
        return mapToResponse(resource);
    }

    private LearningResourceResponse mapToResponse(LearningResource r) {
        Topic topic = r.getTopic();
        Long topicId = topic != null ? topic.getId() : null;
        String topicName = topic != null ? topic.getName() : null;
        Long subjectId = topic != null && topic.getSubject() != null ? topic.getSubject().getId() : null;
        String subjectName = topic != null
                ? (topic.getSubject() != null ? topic.getSubject().getName() : topic.getSubjectName())
                : null;

        return new LearningResourceResponse(
                r.getId(),
                r.getTitle(),
                r.getContentType(),
                r.getResourceUrl(),
                r.getSummary(),
                r.getDifficultyLevel(),
                topicId,
                topicName,
                subjectId,
                subjectName
        );
    }
}
