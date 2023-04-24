package com.example.msi.service.impl;

import com.example.msi.domains.CompanyResultFile;
import com.example.msi.models.companyresult_file.CreateCompanyResultFileDTO;
import com.example.msi.repository.CompanyResultFileRepository;
import com.example.msi.service.CompanyResultFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyResultFileServiceImpl implements CompanyResultFileService {
  private final CompanyResultFileRepository repository;

  @Override
  public void add(@NonNull CreateCompanyResultFileDTO dto) {
    repository.save(CompanyResultFile.getInstance(dto));
  }
}
