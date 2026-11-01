package com.edutech.knowledgegap.service;

import com.edutech.knowledgegap.dto.QuestionResponse;
import com.edutech.knowledgegap.dto.QuizResultResponse;
import com.edutech.knowledgegap.dto.QuizSubmissionRequest;
import com.edutech.knowledgegap.entity.Student;

import java.util.List;
import java.util.Map;

public interface QuizService {

    /**
     * Generates a safe question list (without correct answers) for an assessment.
     * Can generate for a specific topic or a comprehensive subject assessment.
     */
    List<QuestionResponse> generateQuizQuestions(Long topicId, Integer limit);

    /**
     * Generates a safe question list for a subject-wide diagnostic assessment.
     */
    List<QuestionResponse> generateQuizQuestions(Long topicId, Long subjectId, Integer limit);

    /**
     * Evaluates student submitted answers server-side, records attempt,
     * triggers knowledge gap detection, generates recommendations, and returns full breakdown.
     */
    QuizResultResponse submitQuiz(QuizSubmissionRequest request, Student student);

    /**
     * Fetches detailed results and answer review of a previous attempt.
     */
    QuizResultResponse getAttemptResult(Long attemptId, Student student);

    /**
     * Returns the student's historical attempts summary.
     */
    List<Map<String, Object>> getAttemptHistory(Student student);
}
