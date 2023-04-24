package com.example.msi.service;

import com.example.msi.domains.CompanyResult;
import com.example.msi.models.companyresult.CreateCompanyResultDTO;
import com.example.msi.shared.exceptions.MSIException;
import org.springframework.lang.NonNull;

import java.io.IOException;

public interface CompanyResultService {
  CompanyResult create(@NonNull CreateCompanyResultDTO dto) throws MSIException, IOException;
}
