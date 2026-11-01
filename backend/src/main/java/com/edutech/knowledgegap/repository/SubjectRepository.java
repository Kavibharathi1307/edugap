package com.edutech.knowledgegap.repository;

import com.edutech.knowledgegap.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findAllByOrderByOrderIndexAsc();
    List<Subject> findByActiveTrueOrderByOrderIndexAsc();
    Optional<Subject> findByCodeIgnoreCase(String code);
    Optional<Subject> findByNameIgnoreCase(String name);
}
