package com.edutech.knowledgegap.service;

import com.edutech.knowledgegap.dto.AuthResponse;
import com.edutech.knowledgegap.dto.LoginRequest;
import com.edutech.knowledgegap.dto.RegisterRequest;
import com.edutech.knowledgegap.entity.Student;
import com.edutech.knowledgegap.exception.BadRequestException;
import com.edutech.knowledgegap.repository.QuizAttemptRepository;
import com.edutech.knowledgegap.repository.StudentRepository;
import com.edutech.knowledgegap.security.JwtTokenProvider;
import com.edutech.knowledgegap.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private QuizAttemptRepository attemptRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    @DisplayName("Should successfully register a new student and generate JWT")
    void testRegisterSuccess() {
        RegisterRequest req = new RegisterRequest("Rohan Verma", "rohan@edugap.ac.in", "SecretPass123");

        when(studentRepository.existsByEmail("rohan@edugap.ac.in")).thenReturn(false);
        when(passwordEncoder.encode("SecretPass123")).thenReturn("encodedPassword");
        when(tokenProvider.generateTokenForUsername("rohan@edugap.ac.in")).thenReturn("dummy.jwt.token");

        Student savedStudent = new Student("Rohan Verma", "rohan@edugap.ac.in", "encodedPassword");
        savedStudent.setId(101L);
        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

        AuthResponse res = authService.register(req);

        assertNotNull(res);
        assertEquals("dummy.jwt.token", res.getToken());
        assertEquals("rohan@edugap.ac.in", res.getEmail());
        assertEquals(101L, res.getStudentId());
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    @DisplayName("Should reject registration if email is already taken")
    void testRegisterDuplicateEmail() {
        RegisterRequest req = new RegisterRequest("Rohan Verma", "existing@edugap.ac.in", "SecretPass123");
        when(studentRepository.existsByEmail("existing@edugap.ac.in")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> authService.register(req));
        verify(studentRepository, never()).save(any(Student.class));
    }

    @Test
    @DisplayName("Should trim and lowercase email before authentication")
    void testLoginNormalizesEmail() {
        LoginRequest req = new LoginRequest("  ROHAN@EDUGAP.AC.IN  ", "SecretPass123");
        Authentication mockAuth = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuth);
        when(tokenProvider.generateToken(mockAuth)).thenReturn("login.jwt.token");

        Student student = new Student("Rohan Verma", "rohan@edugap.ac.in", "encoded");
        student.setId(101L);
        when(studentRepository.findByEmail("rohan@edugap.ac.in")).thenReturn(Optional.of(student));

        AuthResponse res = authService.login(req);

        assertEquals("rohan@edugap.ac.in", req.getEmail());
        assertEquals("login.jwt.token", res.getToken());
        verify(authenticationManager).authenticate(argThat(auth ->
                "rohan@edugap.ac.in".equals(auth.getPrincipal())));
    }

    @Test
    @DisplayName("Should normalize email when registering")
    void testRegisterNormalizesEmail() {
        RegisterRequest req = new RegisterRequest("  Rohan Verma  ", "  ROHAN@EDUGAP.AC.IN  ", "SecretPass123");

        when(studentRepository.existsByEmail("rohan@edugap.ac.in")).thenReturn(false);
        when(passwordEncoder.encode("SecretPass123")).thenReturn("encodedPassword");
        when(tokenProvider.generateTokenForUsername("rohan@edugap.ac.in")).thenReturn("dummy.jwt.token");

        Student savedStudent = new Student("Rohan Verma", "rohan@edugap.ac.in", "encodedPassword");
        savedStudent.setId(101L);
        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

        AuthResponse res = authService.register(req);

        assertEquals("rohan@edugap.ac.in", res.getEmail());
        verify(studentRepository).existsByEmail("rohan@edugap.ac.in");
    }

    @Test
    @DisplayName("Should authenticate valid credentials and issue JWT")
    void testLoginSuccess() {
        LoginRequest req = new LoginRequest("rohan@edugap.ac.in", "SecretPass123");
        Authentication mockAuth = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mockAuth);
        when(tokenProvider.generateToken(mockAuth)).thenReturn("login.jwt.token");

        Student student = new Student("Rohan Verma", "rohan@edugap.ac.in", "encoded");
        student.setId(101L);
        when(studentRepository.findByEmail("rohan@edugap.ac.in")).thenReturn(Optional.of(student));

        AuthResponse res = authService.login(req);

        assertNotNull(res);
        assertEquals("login.jwt.token", res.getToken());
        assertEquals(101L, res.getStudentId());
    }
}
