package com.edutech.knowledgegap.controller;

import com.edutech.knowledgegap.dto.ApiResponse;
import com.edutech.knowledgegap.dto.LearningResourceResponse;
import com.edutech.knowledgegap.service.ResourceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LearningResourceResponse>>> getAllResources() {
        List<LearningResourceResponse> resources = resourceService.getAllResources();
        return ResponseEntity.ok(ApiResponse.success("Resources retrieved successfully", resources));
    }

    @GetMapping("/{resourceId}")
    public ResponseEntity<ApiResponse<LearningResourceResponse>> getResourceById(@PathVariable Long resourceId) {
        LearningResourceResponse resource = resourceService.getResourceById(resourceId);
        return ResponseEntity.ok(ApiResponse.success("Resource retrieved successfully", resource));
    }
}
