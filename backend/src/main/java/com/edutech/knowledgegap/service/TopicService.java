package com.edutech.knowledgegap.service;

import com.edutech.knowledgegap.dto.LearningResourceResponse;
import com.edutech.knowledgegap.dto.TopicResponse;
import com.edutech.knowledgegap.entity.Student;

import java.util.List;

public interface TopicService {
    List<TopicResponse> getAllTopics(Student student);
    List<TopicResponse> getAllTopics(Student student, Long subjectId);
    TopicResponse getTopicById(Long topicId, Student student);
    List<LearningResourceResponse> getTopicResources(Long topicId);
    TopicResponse markTopicProgress(Long topicId, Student student, boolean completed);
}
