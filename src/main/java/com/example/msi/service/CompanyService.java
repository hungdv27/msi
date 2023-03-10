package com.example.msi.service;

import com.example.msi.domains.Company;
import com.example.msi.exceptions.MSIException;
import com.example.msi.models.company.CreateCompanyDTO;
import com.example.msi.models.company.UpdateCompanyDTO;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.servlet.http.HttpServletRequest;
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
}
