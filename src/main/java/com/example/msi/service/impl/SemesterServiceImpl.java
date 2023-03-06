package com.example.msi.service.impl;

import com.example.msi.domains.Semester;
import com.example.msi.models.company.CreateSemesterDTO;
import com.example.msi.models.company.UpdateSemesterDTO;
import com.example.msi.repository.SemesterRepository;
import com.example.msi.service.SemesterService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SemesterServiceImpl implements SemesterService {
  private final SemesterRepository repository;
  @Override
  public List<Semester> getAllSemester() {
    return repository.findAll();
  }

  @Override
  public void addSemester(CreateSemesterDTO semester) {
    var entity = Semester.getInstance(semester);
    repository.save(entity);
  }

  @Override
  public void updateSemester(@NonNull UpdateSemesterDTO payload) {
    var id=payload.getId();
    repository.findById(id).ifPresent(entity -> {
      entity.update(payload);
      repository.save(entity);
    });
  }

  @Override
  public void deleteSemester(int id) {
    repository.deleteById(id);
  }
}
