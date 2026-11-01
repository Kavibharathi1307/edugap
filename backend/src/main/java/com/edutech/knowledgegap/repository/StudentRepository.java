package com.edutech.knowledgegap.repository;

import com.edutech.knowledgegap.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("select s from Student s where lower(s.email) = lower(:email)")
    Optional<Student> findByEmail(@Param("email") String email);

    @Query("select case when count(s) > 0 then true else false end from Student s where lower(s.email) = lower(:email)")
    boolean existsByEmail(@Param("email") String email);
}
