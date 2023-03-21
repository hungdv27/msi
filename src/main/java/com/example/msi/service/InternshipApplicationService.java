package com.example.msi.service;

import com.example.msi.domains.InternshipApplication;
import com.example.msi.models.internshipappication.CreateInternshipApplicationDTO;
import com.example.msi.models.internshipappication.SearchInternshipApplicationDTO;
import com.example.msi.models.internshipappication.UpdateInternshipApplicationDTO;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface InternshipApplicationService {
  List<InternshipApplication> search(@NonNull SearchInternshipApplicationDTO filter);

  InternshipApplication findById(int id);

  InternshipApplication create(@NonNull CreateInternshipApplicationDTO dto);

  Optional<InternshipApplication> update(@NonNull UpdateInternshipApplicationDTO dto);

  void delete(int id);

}
