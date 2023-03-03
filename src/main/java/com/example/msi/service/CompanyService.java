package com.example.msi.service;

import com.example.msi.domains.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.awt.print.Book;
import java.util.List;

public interface CompanyService {
  Page<Company> getAllCompany(Pageable pageable);

  Company getCompanyById(Integer id);

  Company addCompany(Company company);

  Company updateCompany(Integer id, Company company);

  void deleteCompany(Integer id);
}
