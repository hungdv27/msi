package com.example.msi.service;

import com.example.msi.domains.CompanyResultFile;
import com.example.msi.models.companyresult_file.CreateCompanyResultFileDTO;
import org.springframework.lang.NonNull;

import java.util.List;

public interface CompanyResultFileService {
  void add(@NonNull CreateCompanyResultFileDTO dto);

  List<CompanyResultFile> findByCompanyResultId(int companyResultId);

  void deleteByCompanyResultId(int companyResultId);
}
