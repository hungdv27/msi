package com.example.msi.service;

import com.example.msi.domains.InternshipApplication;
import com.example.msi.domains.InternshipProcess;
import com.example.msi.models.internshipprocess.AssignTeacherDTO;
import com.example.msi.models.internshipprocess.CreateInternshipProcessDTO;
import com.example.msi.models.internshipprocess.SearchInternshipProcessDTO;
import com.example.msi.shared.exceptions.MSIException;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface InternshipProcessService {
  void assignTeacher(@NonNull AssignTeacherDTO dto, @NonNull String username) throws MSIException;

  Optional<InternshipProcess> findByApplicationId(int applicationId);

  Optional<InternshipProcess> findById(int id);

  long currentWeekProcess(InternshipApplication internshipApplication);

  Page<InternshipProcess> search(@NonNull SearchInternshipProcessDTO filter);

  InternshipProcess create(@NonNull CreateInternshipProcessDTO dto);

  Object export(HttpServletRequest request, Object req) throws MSIException;
}
