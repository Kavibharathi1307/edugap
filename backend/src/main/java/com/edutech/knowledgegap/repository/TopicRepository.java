package com.edutech.knowledgegap.repository;

import com.edutech.knowledgegap.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findAllByOrderByOrderIndexAsc();
    Optional<Topic> findByNameIgnoreCase(String name);
    List<Topic> findBySubjectNameIgnoreCaseOrderByOrderIndexAsc(String subjectName);
    List<Topic> findBySubjectIdOrderByOrderIndexAsc(Long subjectId);
    List<Topic> findBySubjectIsNull();
    long countBySubjectId(Long subjectId);
}
