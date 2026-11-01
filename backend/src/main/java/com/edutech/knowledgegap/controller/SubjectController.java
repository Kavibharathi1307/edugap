package com.edutech.knowledgegap.controller;

import com.edutech.knowledgegap.dto.ApiResponse;
import com.edutech.knowledgegap.dto.SubjectDetailResponse;
import com.edutech.knowledgegap.dto.SubjectResponse;
import com.edutech.knowledgegap.entity.Student;
import com.edutech.knowledgegap.service.AuthService;
import com.edutech.knowledgegap.service.SubjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService subjectService;
    private final AuthService authService;

    public SubjectController(SubjectService subjectService, AuthService authService) {
        this.subjectService = subjectService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SubjectResponse>>> getSubjects() {
        Student current = authService.getAuthenticatedStudent();
        return ResponseEntity.ok(ApiResponse.success("Subjects retrieved successfully",
                subjectService.getSubjects(current)));
    }

    @GetMapping("/{subjectId}")
    public ResponseEntity<ApiResponse<SubjectDetailResponse>> getSubject(@PathVariable Long subjectId) {
        Student current = authService.getAuthenticatedStudent();
        return ResponseEntity.ok(ApiResponse.success("Subject details retrieved successfully",
                subjectService.getSubjectDetail(subjectId, current)));
    }
}
