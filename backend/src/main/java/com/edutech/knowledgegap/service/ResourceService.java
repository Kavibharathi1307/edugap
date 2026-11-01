package com.edutech.knowledgegap.service;

import com.edutech.knowledgegap.dto.LearningResourceResponse;

import java.util.List;

public interface ResourceService {
    List<LearningResourceResponse> getAllResources();
    LearningResourceResponse getResourceById(Long resourceId);
}
