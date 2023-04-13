package com.example.msi.service;


import com.example.msi.domains.Teacher;
import com.example.msi.models.teacher.SearchTeacherDTO;
import com.example.msi.models.teacher.UpdateTeacherDTO;
import com.example.msi.shared.exceptions.MSIException;
import lombok.NonNull;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface TeacherService {
  void updateTeacher(@NonNull UpdateTeacherDTO payload, String userName) throws MSIException;

  Optional<Teacher> findByUsername(String userName) throws MSIException;

  Page<Teacher> search(@NonNull SearchTeacherDTO filter) throws MSIException;

  Teacher save(@NonNull Teacher teacher);
}
