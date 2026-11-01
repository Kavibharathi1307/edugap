package com.edutech.knowledgegap.dto;

public class AuthResponse {

    private String token;
    private String tokenType = "Bearer";
    private Long studentId;
    private String fullName;
    private String email;
    private String role;

    public AuthResponse() {}

    public AuthResponse(String token, Long studentId, String fullName, String email, String role) {
        this.token = token;
        this.studentId = studentId;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
