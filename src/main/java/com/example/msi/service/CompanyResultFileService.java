package com.example.msi.service;

import com.example.msi.models.companyresult_file.CreateCompanyResultFileDTO;
import org.springframework.lang.NonNull;

public interface CompanyResultFileService {
  void add(@NonNull CreateCompanyResultFileDTO dto);
}
