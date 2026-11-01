package com.edutech.knowledgegap.controller;

import com.edutech.knowledgegap.dto.ApiResponse;
import com.edutech.knowledgegap.dto.QuestionResponse;
import com.edutech.knowledgegap.dto.QuizResultResponse;
import com.edutech.knowledgegap.dto.QuizSubmissionRequest;
import com.edutech.knowledgegap.entity.Student;
import com.edutech.knowledgegap.service.AuthService;
import com.edutech.knowledgegap.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;
    private final AuthService authService;

    public QuizController(QuizService quizService, AuthService authService) {
        this.quizService = quizService;
        this.authService = authService;
    }

    @GetMapping("/questions")
    public ResponseEntity<ApiResponse<List<QuestionResponse>>> getQuizQuestions(
            @RequestParam(required = false) Long topicId,
            @RequestParam(required = false) Long subjectId,
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        List<QuestionResponse> questions = quizService.generateQuizQuestions(topicId, subjectId, limit);
        return ResponseEntity.ok(ApiResponse.success("Assessment questions loaded", questions));
    }

    @PostMapping("/submit")
    public ResponseEntity<ApiResponse<QuizResultResponse>> submitQuiz(
            @Valid @RequestBody QuizSubmissionRequest request) {
        Student current = authService.getAuthenticatedStudent();
        QuizResultResponse result = quizService.submitQuiz(request, current);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Assessment evaluated successfully", result));
    }

    @GetMapping("/attempts/{attemptId}")
    public ResponseEntity<ApiResponse<QuizResultResponse>> getAttemptResult(@PathVariable Long attemptId) {
        Student current = authService.getAuthenticatedStudent();
        QuizResultResponse result = quizService.getAttemptResult(attemptId, current);
        return ResponseEntity.ok(ApiResponse.success("Assessment details retrieved", result));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<Map<String, Object>>>> getAttemptHistory() {
        Student current = authService.getAuthenticatedStudent();
        List<Map<String, Object>> history = quizService.getAttemptHistory(current);
        return ResponseEntity.ok(ApiResponse.success("Assessment history retrieved", history));
    }
}
