package com.example.msi.service;

import com.example.msi.domains.CompanyResult;
import com.example.msi.models.companyresult.CreateCompanyResultDTO;
import com.example.msi.shared.exceptions.MSIException;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CompanyResultService {
  CompanyResult create(@NonNull CreateCompanyResultDTO dto) throws MSIException, IOException;

  void delete(int id);

  Optional<CompanyResult> findByStudentCode(String code);

  List<CompanyResult> findAll();

  byte[] templateDownload(HttpServletRequest request) throws IOException;

  String importFile(MultipartFile file, HttpServletRequest request) throws IOException, MSIException;
}
