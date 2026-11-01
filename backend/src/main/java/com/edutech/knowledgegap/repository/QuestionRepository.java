package com.edutech.knowledgegap.repository;

import com.edutech.knowledgegap.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByTopicId(Long topicId);

    @Query("SELECT q FROM Question q JOIN FETCH q.topic ORDER BY FUNCTION('RANDOM')")
    List<Question> findRandomQuestions();

    @Query("SELECT q FROM Question q JOIN FETCH q.topic WHERE q.topic.id = :topicId")
    List<Question> findByTopicIdWithTopic(Long topicId);

    @Query("SELECT q FROM Question q JOIN FETCH q.topic t LEFT JOIN FETCH t.subject WHERE t.subject.id = :subjectId")
    List<Question> findBySubjectIdWithTopic(Long subjectId);

    long countByTopicId(Long topicId);
}
