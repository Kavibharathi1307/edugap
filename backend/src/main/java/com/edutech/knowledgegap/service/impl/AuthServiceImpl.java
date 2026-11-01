package com.edutech.knowledgegap.service.impl;

import com.edutech.knowledgegap.dto.AuthResponse;
import com.edutech.knowledgegap.dto.LoginRequest;
import com.edutech.knowledgegap.dto.RegisterRequest;
import com.edutech.knowledgegap.dto.StudentProfileResponse;
import com.edutech.knowledgegap.entity.QuizAttempt;
import com.edutech.knowledgegap.entity.Student;
import com.edutech.knowledgegap.exception.BadRequestException;
import com.edutech.knowledgegap.exception.ResourceNotFoundException;
import com.edutech.knowledgegap.repository.QuizAttemptRepository;
import com.edutech.knowledgegap.repository.StudentRepository;
import com.edutech.knowledgegap.security.JwtTokenProvider;
import com.edutech.knowledgegap.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    private final StudentRepository studentRepository;
    private final QuizAttemptRepository attemptRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;

    public AuthServiceImpl(StudentRepository studentRepository,
                           QuizAttemptRepository attemptRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtTokenProvider tokenProvider) {
        this.studentRepository = studentRepository;
        this.attemptRepository = attemptRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String normalizedEmail = request.getEmail().toLowerCase().trim();

        if (studentRepository.existsByEmail(normalizedEmail)) {
            throw new BadRequestException("An account with this email address already exists.");
        }

        Student student = new Student(
                request.getFullName().trim(),
                normalizedEmail,
                passwordEncoder.encode(request.getPassword())
        );

        Student saved = studentRepository.save(student);
        String token = tokenProvider.generateTokenForUsername(saved.getEmail());

        return new AuthResponse(
                token,
                saved.getId(),
                saved.getFullName(),
                saved.getEmail(),
                saved.getRole()
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        String normalizedEmail = request.getEmail().toLowerCase().trim();

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(normalizedEmail, request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Student student = studentRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        String token = tokenProvider.generateToken(authentication);

        return new AuthResponse(
                token,
                student.getId(),
                student.getFullName(),
                student.getEmail(),
                student.getRole()
        );
    }

    @Override
    public StudentProfileResponse getCurrentStudentProfile(String email) {
        Student student = studentRepository.findByEmail(email.toLowerCase().trim())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        List<QuizAttempt> attempts = attemptRepository.findByStudentIdOrderByCompletedAtDesc(student.getId());
        long totalAttempts = attempts.size();
        double avgScore = attempts.isEmpty() ? 0.0 :
                attempts.stream().mapToDouble(QuizAttempt::getScorePercentage).average().orElse(0.0);

        return new StudentProfileResponse(
                student.getId(),
                student.getFullName(),
                student.getEmail(),
                student.getRole(),
                student.getCreatedAt(),
                totalAttempts,
                Math.round(avgScore * 10.0) / 10.0
        );
    }

    @Override
    public Student getAuthenticatedStudent() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new BadRequestException("User is not authenticated");
        }
        String email = auth.getName();
        return studentRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated student record not found"));
    }
}
