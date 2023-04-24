package com.example.msi.service.impl;

import com.example.msi.domains.CompanyResultFile;
import com.example.msi.models.companyresult_file.CreateCompanyResultFileDTO;
import com.example.msi.repository.CompanyResultFileRepository;
import com.example.msi.service.CompanyResultFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyResultFileServiceImpl implements CompanyResultFileService {
  private final CompanyResultFileRepository repository;

  @Override
  public void add(@NonNull CreateCompanyResultFileDTO dto) {
    repository.save(CompanyResultFile.getInstance(dto));
  }

  @Override
  public List<CompanyResultFile> findByCompanyResultId(int companyResultId) {
    return repository.findAllByCompanyResultId(companyResultId);
  }

  @Override
  @Transactional
  public void deleteByCompanyResultId(int companyResultId) {
    repository.deleteAllByCompanyResultId(companyResultId);
  }
}
