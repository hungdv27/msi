package com.example.msi.service.impl;

import com.example.msi.domains.Grade;
import com.example.msi.models.grade.CreateGradeDTO;
import com.example.msi.models.grade.UpdateGradeDTO;
import com.example.msi.repository.GradeRepository;
import com.example.msi.service.GradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {
  private final GradeRepository repository;

  @Override
  public List<Grade> getAllGrade() {
    return repository.findAll();
  }

  @Override
  public void addGrade(@NonNull CreateGradeDTO grade) {
    var entity = Grade.getInstance(grade);
    repository.save(entity);
  }

  @Override
  public void updateGrade(@NonNull UpdateGradeDTO payload) {
    var id = payload.getId();
    repository.findById(id).ifPresent(entity -> {
      entity.update(payload);
      repository.save(entity);
    });
  }

  @Override
  public void deleteGrade(int id) {
    repository.deleteById(id);
  }
}
