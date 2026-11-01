package com.edutech.knowledgegap.controller;

import com.edutech.knowledgegap.dto.ApiResponse;
import com.edutech.knowledgegap.dto.LearningRecommendationResponse;
import com.edutech.knowledgegap.entity.Student;
import com.edutech.knowledgegap.service.AuthService;
import com.edutech.knowledgegap.service.PersonalizedRecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final PersonalizedRecommendationService recommendationService;
    private final AuthService authService;

    public RecommendationController(PersonalizedRecommendationService recommendationService,
                                    AuthService authService) {
        this.recommendationService = recommendationService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<LearningRecommendationResponse>>> getActiveRecommendations() {
        Student current = authService.getAuthenticatedStudent();
        List<LearningRecommendationResponse> recs = recommendationService.getActiveRecommendations(current);
        return ResponseEntity.ok(ApiResponse.success("Recommendations retrieved", recs));
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<ApiResponse<LearningRecommendationResponse>> markCompleted(@PathVariable Long id) {
        Student current = authService.getAuthenticatedStudent();
        LearningRecommendationResponse updated = recommendationService.markAsCompleted(id, current);
        return ResponseEntity.ok(ApiResponse.success("Recommendation marked as completed", updated));
    }
}
