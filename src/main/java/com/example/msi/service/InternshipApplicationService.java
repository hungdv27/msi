package com.example.msi.service;

import com.example.msi.domains.InternshipApplication;
import com.example.msi.models.internshipappication.CreateInternshipApplicationDTO;
import com.example.msi.models.internshipappication.SearchInternshipApplicationDTO;
import com.example.msi.models.internshipappication.UpdateInternshipApplicationDTO;
import com.example.msi.models.internshipappication.VerifyApplicationDTO;
import com.example.msi.shared.enums.InternshipApplicationStatus;
import com.example.msi.shared.exceptions.MSIException;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface InternshipApplicationService {
  Page<InternshipApplication> search(@NonNull SearchInternshipApplicationDTO filter);

  InternshipApplication findById(int id);

  List<InternshipApplication> findByStudentCCode(@NonNull String studentCode);

  InternshipApplication create(@NonNull CreateInternshipApplicationDTO dto) throws MSIException, IOException;

  Optional<InternshipApplication> update(@NonNull UpdateInternshipApplicationDTO dto);

  void delete(int id);

  void verify(@NonNull VerifyApplicationDTO dto) throws Exception;

  Optional<InternshipApplication> regis(int id);

  Optional<InternshipApplication> cancelRegis(int id);

  Optional<InternshipApplication> findByStudentCodeAndStatus(String studentCode, InternshipApplicationStatus status);

  boolean existsBySemesterId(int semesterId);
}
