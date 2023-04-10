package com.example.msi.service;

import com.example.msi.domains.InternshipProcess;
import com.example.msi.models.internshipprocess.AssignTeacherDTO;
import com.example.msi.shared.exceptions.MSIException;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface InternshipProcessService {
  void assignTeacher(@NonNull AssignTeacherDTO dto, @NonNull String username) throws MSIException;

  Optional<InternshipProcess> findByApplicationId(int applicationId);
}
