# REST API Documentation

Base URL: `http://localhost:8080/api` (Local) / `/api` (Production)

All protected endpoints require the HTTP Header:
`Authorization: Bearer <jwt-token>`

---

## 1. Authentication Endpoints (`/api/auth`)

### 1.1 Register Student
- **Method:** `POST /api/auth/register`
- **Access:** Public
- **Request Body:**
```json
{
  "fullName": "Priya Sharma",
  "email": "priya@college.edu",
  "password": "Password@123"
}
```
- **Response (`201 Created`):**
```json
{
  "success": true,
  "message": "Student registered successfully",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer",
    "studentId": 1,
    "fullName": "Priya Sharma",
    "email": "priya@college.edu",
    "role": "ROLE_STUDENT"
  }
}
```

### 1.2 Student Login
- **Method:** `POST /api/auth/login`
- **Access:** Public
- **Request Body:**
```json
{
  "email": "student@edugap.ac.in",
  "password": "Password@123"
}
```
- **Response (`200 OK`):**
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "studentId": 1,
    "fullName": "Priya Sharma",
    "email": "student@edugap.ac.in"
  }
}
```

### 1.3 Current Student Profile
- **Method:** `GET /api/auth/me`
- **Access:** Protected

---

## 2. Topic Endpoints (`/api/topics`)

### 2.1 List All Curriculum Topics
- **Method:** `GET /api/topics`
- **Access:** Protected
- **Response (`200 OK`):**
Returns array of 8 topics with order index, description, completion status, and learning resources.

### 2.2 Get Topic Details
- **Method:** `GET /api/topics/{topicId}`
- **Access:** Protected

### 2.3 Toggle Topic Study Completion
- **Method:** `POST /api/topics/{topicId}/progress`
- **Access:** Protected
- **Request Body:** `{"completed": true}`

---

## 3. Quiz & Assessment Endpoints (`/api/quizzes`)

### 3.1 Fetch Quiz Questions
- **Method:** `GET /api/quizzes/questions?topicId={optional}&limit={optional}`
- **Access:** Protected
- **Security:** Correct answer flags (`isCorrect`) are withheld from response.

### 3.2 Submit Quiz for Evaluation
- **Method:** `POST /api/quizzes/submit`
- **Access:** Protected
- **Request Body:**
```json
{
  "subjectName": "Core Java Diagnostic",
  "answers": [
    { "questionId": 1, "selectedOptionKey": "B" },
    { "questionId": 2, "selectedOptionKey": "C" }
  ]
}
```
- **Response (`201 Created`):**
Returns attempt record, overall score, question-by-question review with explanations, topic-wise accuracy, detected knowledge gaps, and generated recommendations.

### 3.3 Get Attempt Results
- **Method:** `GET /api/quizzes/attempts/{attemptId}`
- **Access:** Protected

### 3.4 Get Attempt History
- **Method:** `GET /api/quizzes/history`
- **Access:** Protected

---

## 4. Analytics Endpoints (`/api/analytics`)

### 4.1 Dashboard Overview
- **Method:** `GET /api/analytics/dashboard`
- **Access:** Protected
- **Returns:** Total attempts, average score, strongest topics, identified gaps, unassessed topics, and radar chart metrics.

### 4.2 Topic Mastery Overview
- **Method:** `GET /api/analytics/topics`
- **Access:** Protected

### 4.3 Progress Trajectory Curve
- **Method:** `GET /api/analytics/progress`
- **Access:** Protected
- **Returns:** Consecutive score series and topic trends for Line Charts.

---

## 5. Recommendation Endpoints (`/api/recommendations`)

### 5.1 Get Active Recommendations
- **Method:** `GET /api/recommendations`
- **Access:** Protected

### 5.2 Mark Recommendation Completed
- **Method:** `PATCH /api/recommendations/{id}/complete`
- **Access:** Protected

---

## 6. System Health (`/api/health`)
- **Method:** `GET /api/health`
- **Access:** Public
- **Returns:** `{"status": "UP", "version": "1.0.0"}`
