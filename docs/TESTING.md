# Testing & Quality Assurance Document

## 1. Automated Test Suites

### 1.1 Backend Unit & Integration Tests (JUnit 5 + Mockito)
Automated tests are located in `backend/src/test/java/com/edutech/knowledgegap/`.

#### Running Tests:
```bash
cd backend
mvnw.cmd test
```

#### Test Execution Summary:
```text
[INFO] Running com.edutech.knowledgegap.service.AuthServiceTest
[INFO] Tests run: 3, Failures: 0, Errors: 0, Skipped: 0
[INFO] Running com.edutech.knowledgegap.service.KnowledgeGapDetectionServiceTest
[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] Results:
[INFO] Tests run: 5, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

### 1.2 Test Cases Covered
1. **`KnowledgeGapDetectionServiceTest.testDetermineGapStatus`**:
   - Asserts unassessed topics (0 questions attempted) evaluate to `NOT_ASSESSED`.
   - Asserts scores $< 40\%$ evaluate to `HIGH_PRIORITY_GAP`.
   - Asserts scores $40\% \le S < 70\%$ evaluate to `NEEDS_IMPROVEMENT`.
   - Asserts scores $70\% \le S < 85\%$ evaluate to `GOOD_UNDERSTANDING`.
   - Asserts scores $\ge 85\%$ evaluate to `STRONG_UNDERSTANDING`.
2. **`KnowledgeGapDetectionServiceTest.testAnalyzeAndPersistGaps`**:
   - Verifies multi-topic aggregation, percentage calculation, and database persistence of `TopicPerformance` records.
3. **`AuthServiceTest.testRegisterSuccess`**:
   - Verifies BCrypt password hashing, repository save, and JWT token issuance.
4. **`AuthServiceTest.testRegisterDuplicateEmail`**:
   - Confirms that duplicate email registration throws `BadRequestException`.
5. **`AuthServiceTest.testLoginSuccess`**:
   - Confirms proper authentication and token issuance for valid credentials.

---

## 2. Frontend Production Build Verification
The frontend code was built and bundled using Vite to confirm zero syntax errors, missing dependencies, or broken JSX tags:

```bash
cd frontend
npm run build
```

#### Output:
```text
vite v5.4.21 building for production...
✓ 1649 modules transformed.
dist/index.html                   1.07 kB │ gzip:   0.61 kB
dist/assets/index-BlyqI2Vb.css   31.96 kB │ gzip:   5.84 kB
dist/assets/index-CzIiE_Zl.js   497.45 kB │ gzip: 156.66 kB
✓ built in 7.47s
```

---

## 3. End-to-End Live Workflow Verification
The following end-to-end integration tests were performed and verified against the running application:

| Action Tested | Endpoint | Observed Output | Result |
| :--- | :--- | :--- | :--- |
| **System Health** | `GET /api/health` | HTTP 200 `{"status": "UP"}` | ✅ PASS |
| **Student Login** | `POST /api/auth/login` | HTTP 200 with Bearer JWT for Priya Sharma | ✅ PASS |
| **Curriculum Fetch** | `GET /api/topics` | HTTP 200 with 8 Core Java modules | ✅ PASS |
| **Quiz Generation** | `GET /api/quizzes/questions` | HTTP 200 with 8 questions (answers stripped) | ✅ PASS |
| **Answer Evaluation** | `POST /api/quizzes/submit` | HTTP 201 with score 37.5%, 4 gaps detected | ✅ PASS |
| **Gap Detection** | DB `topic_performances` | Correctly assigned High-Priority Gaps (<40%) | ✅ PASS |
| **Recommendation** | DB `learning_recommendations`| Remedial notes & follow-up quizzes created | ✅ PASS |
| **Delta Growth** | `POST /api/quizzes/submit` | Retook Arrays with 100%, score updated $\Delta > 0$ | ✅ PASS |
| **Chart Data** | `GET /api/analytics/progress` | Returned progression series: `[37.5, 100.0]` | ✅ PASS |
