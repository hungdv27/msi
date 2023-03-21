package com.example.msi.service;

import com.example.msi.domains.Company;
import com.example.msi.shared.exceptions.MSIException;
import com.example.msi.models.company.CreateCompanyDTO;
import com.example.msi.models.company.UpdateCompanyDTO;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;


public interface CompanyService {
  Page<Company> getAllCompany(Pageable pageable);

  Company getCompanyById(int id);

  Company addCompany(CreateCompanyDTO company);

  Optional<Company> updateCompany(@NonNull UpdateCompanyDTO payload);

  List<Company> searchCompanyByName(@NonNull String name);

  void deleteCompany(int id);

  Object export(HttpServletRequest request, Object req) throws MSIException;

  byte[] templateDownload(HttpServletRequest request) throws IOException;

  String importFile(MultipartFile file, HttpServletRequest request) throws IOException, MSIException;
}
