package com.edutech.knowledgegap.config;

import com.edutech.knowledgegap.entity.Student;
import com.edutech.knowledgegap.entity.Subject;
import com.edutech.knowledgegap.repository.LearningRecommendationRepository;
import com.edutech.knowledgegap.repository.LearningResourceRepository;
import com.edutech.knowledgegap.repository.QuestionOptionRepository;
import com.edutech.knowledgegap.repository.QuestionRepository;
import com.edutech.knowledgegap.repository.QuizAttemptRepository;
import com.edutech.knowledgegap.repository.StudentRepository;
import com.edutech.knowledgegap.repository.SubjectRepository;
import com.edutech.knowledgegap.repository.TopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataSeederTest {

    private static final String DEMO_EMAIL = "student@edugap.ac.in";
    private static final String DEMO_PASSWORD = "Password@123";

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private TopicRepository topicRepository;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private QuestionOptionRepository optionRepository;

    @Mock
    private LearningResourceRepository resourceRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private QuizAttemptRepository attemptRepository;

    @Mock
    private LearningRecommendationRepository recommendationRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private DataSeeder dataSeeder;

    @BeforeEach
    void setUp() {
        dataSeeder = new DataSeeder(
                subjectRepository,
                topicRepository,
                questionRepository,
                optionRepository,
                resourceRepository,
                studentRepository,
                attemptRepository,
                recommendationRepository,
                passwordEncoder
        );
        when(subjectRepository.findByCodeIgnoreCase(anyString())).thenAnswer(invocation -> {
            String code = invocation.getArgument(0);
            Subject subject = new Subject(code, code + " Subject", "", "", 1);
            subject.setId(1L);
            return Optional.of(subject);
        });
        when(topicRepository.countBySubjectId(anyLong())).thenReturn(1L);
    }

    @Test
    @DisplayName("Should create the demo student when it is missing")
    void shouldCreateDemoStudentWhenMissing() {
        when(studentRepository.findByEmail(DEMO_EMAIL)).thenReturn(Optional.empty());

        dataSeeder.run();

        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(captor.capture());
        Student saved = captor.getValue();
        assertTrue(passwordEncoder.matches(DEMO_PASSWORD, saved.getPassword()));
    }

    @Test
    @DisplayName("Should repair the demo password when the stored hash does not match the displayed credentials")
    void shouldRepairIncorrectDemoPassword() {
        Student existing = new Student("Priya Sharma", DEMO_EMAIL, passwordEncoder.encode("OldPassword@123"));
        existing.setId(1L);
        when(studentRepository.findByEmail(DEMO_EMAIL)).thenReturn(Optional.of(existing));

        dataSeeder.run();

        ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(captor.capture());
        assertTrue(passwordEncoder.matches(DEMO_PASSWORD, captor.getValue().getPassword()));
    }

    @Test
    @DisplayName("Should leave a matching demo password unchanged")
    void shouldLeaveMatchingDemoPasswordUnchanged() {
        Student existing = new Student("Priya Sharma", DEMO_EMAIL, passwordEncoder.encode(DEMO_PASSWORD));
        existing.setId(1L);
        when(studentRepository.findByEmail(DEMO_EMAIL)).thenReturn(Optional.of(existing));

        dataSeeder.run();

        verify(studentRepository, never()).save(any(Student.class));
    }
}
