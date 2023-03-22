package com.example.msi.service.impl;

import com.example.msi.domains.InternshipApplication;
import com.example.msi.models.internshipappication.CreateInternshipApplicationDTO;
import com.example.msi.models.internshipappication.SearchInternshipApplicationDTO;
import com.example.msi.models.internshipappication.UpdateInternshipApplicationDTO;
import com.example.msi.repository.InternshipApplicationRepository;
import com.example.msi.service.InternshipApplicationService;
import com.example.msi.shared.exceptions.MSIException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InternshipApplicationServiceImpl implements InternshipApplicationService {
  private final InternshipApplicationRepository repository;

  @Override
  public Page<InternshipApplication> search(@NonNull SearchInternshipApplicationDTO filter) {
    var spec = filter.getSpecification();
    var pageable = filter.getPageable();
    return repository.findAll(spec, pageable);
  }

  @Override
  public InternshipApplication findById(int id) {
    return repository.findById(id).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public InternshipApplication create(@NonNull CreateInternshipApplicationDTO dto) throws MSIException {
    var entity = InternshipApplication.getInstance(dto);
    return repository.save(entity);
  }

  @Override
  public Optional<InternshipApplication> update(@NonNull UpdateInternshipApplicationDTO dto) {
    return repository.findById(dto.getId()).map(entity -> {
      entity.update(dto);
      return repository.save(entity);
    });
  }

  @Override
  public void delete(int id) {
    repository.deleteById(id);
  }
}
