package com.example.msi.service;

import com.example.msi.models.internshipprocess.AssignTeacherDTO;
import com.example.msi.shared.exceptions.MSIException;
import org.springframework.lang.NonNull;

public interface InternshipProcessService {
  void assignTeacher(@NonNull AssignTeacherDTO dto, @NonNull String username) throws MSIException;
}
