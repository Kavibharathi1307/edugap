package com.edutech.knowledgegap.repository;

import com.edutech.knowledgegap.entity.QuestionOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionOptionRepository extends JpaRepository<QuestionOption, Long> {
    List<QuestionOption> findByQuestionId(Long questionId);
    Optional<QuestionOption> findByQuestionIdAndOptionKey(Long questionId, String optionKey);
    Optional<QuestionOption> findByQuestionIdAndIsCorrectTrue(Long questionId);
}
