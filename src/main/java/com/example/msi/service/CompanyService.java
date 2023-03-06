package com.example.msi.service;

import com.example.msi.domains.Company;
import com.example.msi.models.company.CreateCompanyDTO;
import com.example.msi.models.company.UpdateCompanyDTO;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface CompanyService {
  Page<Company> getAllCompany(Pageable pageable);

  Company getCompanyById(Integer id);

  Company addCompany(CreateCompanyDTO company);

  Optional<Company> updateCompany(@NonNull UpdateCompanyDTO payload);

  void deleteCompany(Integer id);
}
