package com.example.msi.service.impl;

import com.example.msi.domains.Major;
import com.example.msi.exceptions.MSIException;
import com.example.msi.models.major.CreateMajorDTO;
import com.example.msi.models.major.UpdateMajorDTO;
import com.example.msi.repository.MajorRepository;
import com.example.msi.service.MajorService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MajorServiceImpl implements MajorService {
  private final MajorRepository repository;

  @Override
  public List<Major> getAllMajor() throws MSIException {
    return repository.findAll();
  }

  @Override
  public void addMajor(CreateMajorDTO major) throws MSIException {
    var entity = Major.getInstance(major);
    repository.save(entity);
  }

  @Override
  public void updateMajor(@NonNull UpdateMajorDTO payload) throws MSIException {
    var id=payload.getId();
    repository.findById(id).ifPresent(entity -> {
      entity.update(payload);
      repository.save(entity);
    });
  }

  @Override
  public void deleteMajor(int id) throws MSIException {
    repository.deleteById(id);
  }
}
