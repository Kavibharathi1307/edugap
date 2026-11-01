package com.edutech.knowledgegap.service;

import com.edutech.knowledgegap.dto.SubjectDetailResponse;
import com.edutech.knowledgegap.dto.SubjectProgressResponse;
import com.edutech.knowledgegap.dto.SubjectResponse;
import com.edutech.knowledgegap.entity.Student;

import java.util.List;

public interface SubjectService {

    List<SubjectResponse> getSubjects(Student student);

    SubjectDetailResponse getSubjectDetail(Long subjectId, Student student);

    List<SubjectProgressResponse> getSubjectProgress(Student student);
}
