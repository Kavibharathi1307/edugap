package com.edutech.knowledgegap.controller;

import com.edutech.knowledgegap.entity.Student;
import com.edutech.knowledgegap.repository.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Should register a student, log in with the same credentials, and open the dashboard")
    void testRegisterThenLogin() throws Exception {
        String email = "Integration.User." + UUID.randomUUID() + "@Example.COM";
        String normalizedEmail = email.toLowerCase();
        String password = "TestPass@123";

        String registerBody = objectMapper.writeValueAsString(Map.of(
                "fullName", "Integration Test Student",
                "email", email,
                "password", password
        ));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value(normalizedEmail))
                .andExpect(jsonPath("$.data.token").isNotEmpty());

        Student saved = studentRepository.findByEmail(normalizedEmail).orElseThrow();
        assertNotEquals(password, saved.getPassword());
        assertTrue(saved.getPassword().startsWith("$2"));
        assertTrue(passwordEncoder.matches(password, saved.getPassword()));

        String loginBody = objectMapper.writeValueAsString(Map.of(
                "email", email.toUpperCase(),
                "password", password
        ));

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value(normalizedEmail))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andReturn();

        String token = objectMapper.readTree(loginResult.getResponse().getContentAsString())
                .path("data").path("token").asText();

        mockMvc.perform(get("/api/auth/me")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value(normalizedEmail));

        mockMvc.perform(get("/api/analytics/dashboard")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Should authenticate the seeded demo account with the credentials shown in the UI")
    void testSeededDemoAccountCanLoginAndAccessDashboard() throws Exception {
        String loginBody = objectMapper.writeValueAsString(Map.of(
                "email", "student@edugap.ac.in",
                "password", "Password@123"
        ));

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("student@edugap.ac.in"))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andReturn();

        String token = objectMapper.readTree(loginResult.getResponse().getContentAsString())
                .path("data").path("token").asText();

        mockMvc.perform(get("/api/analytics/dashboard")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @DisplayName("Should return seeded subjects and topics for the demo account")
    void testSeededCurriculumEndpointsReturnData() throws Exception {
        String loginBody = objectMapper.writeValueAsString(Map.of(
                "email", "student@edugap.ac.in",
                "password", "Password@123"
        ));

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andReturn();

        String token = objectMapper.readTree(loginResult.getResponse().getContentAsString())
                .path("data").path("token").asText();

        mockMvc.perform(get("/api/subjects")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data[0].topicCount").isNumber());

        mockMvc.perform(get("/api/topics")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andExpect(jsonPath("$.data[0].subjectId").isNumber())
                .andExpect(jsonPath("$.data[0].subjectName").isNotEmpty());
    }

    @Test
    @DisplayName("Should reject the demo account with an incorrect password")
    void testDemoAccountRejectsIncorrectPassword() throws Exception {
        String loginBody = objectMapper.writeValueAsString(Map.of(
                "email", "student@edugap.ac.in",
                "password", "WrongPassword@123"
        ));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }

    @Test
    @DisplayName("Should log in when an existing student email is stored with mixed case")
    void testLoginWithLegacyMixedCaseEmail() throws Exception {
        String storedEmail = "Legacy.User." + UUID.randomUUID() + "@Example.COM";
        String password = "LegacyPass@123";

        studentRepository.save(new Student(
                "Legacy Test Student",
                storedEmail,
                passwordEncoder.encode(password)
        ));

        String loginBody = objectMapper.writeValueAsString(Map.of(
                "email", storedEmail.toLowerCase(),
                "password", password
        ));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").isNotEmpty());
    }
}
