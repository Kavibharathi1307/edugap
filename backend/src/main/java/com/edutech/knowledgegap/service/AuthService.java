package com.edutech.knowledgegap.service;

import com.edutech.knowledgegap.dto.AuthResponse;
import com.edutech.knowledgegap.dto.LoginRequest;
import com.edutech.knowledgegap.dto.RegisterRequest;
import com.edutech.knowledgegap.dto.StudentProfileResponse;
import com.edutech.knowledgegap.entity.Student;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    StudentProfileResponse getCurrentStudentProfile(String email);
    Student getAuthenticatedStudent();
}
