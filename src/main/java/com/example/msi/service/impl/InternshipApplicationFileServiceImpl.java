package com.example.msi.service.impl;

import com.example.msi.domains.InternshipApplicationFile;
import com.example.msi.models.internshipapplication_file.CreateInternshipApplicationFileDTO;
import com.example.msi.repository.InternshipApplicationFileRepository;
import com.example.msi.service.InternshipApplicationFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InternshipApplicationFileServiceImpl implements InternshipApplicationFileService {
  private final InternshipApplicationFileRepository repository;

  @Override
  public List<InternshipApplicationFile> findByInternshipApplicationId(int internshipApplicationFileId) {
    return repository.findAllByInternshipApplicationId(internshipApplicationFileId);
  }

  @Override
  public void add(@NonNull CreateInternshipApplicationFileDTO dto) {
    repository.save(InternshipApplicationFile.getInstance(dto));
  }

  @Override
  public void deleteByInternshipApplicationId(int internshipApplicationId) {
    repository.deleteAllByInternshipApplicationId(internshipApplicationId);
  }
}
