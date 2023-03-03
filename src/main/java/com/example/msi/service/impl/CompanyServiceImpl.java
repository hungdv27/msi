package com.example.msi.service.impl;

import com.example.msi.domains.Company;
import com.example.msi.repository.CompanyRepository;
import com.example.msi.service.CompanyService;
import com.example.msi.shared.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
  private final CompanyRepository repository;

  private static final int DEFAULT_PAGE_NUMBER = 0;
  private static final int DEFAULT_PAGE_SIZE = 10;

  public static Pageable getPageable(Integer pageNumber, Integer pageSize) {
    pageNumber = pageNumber != null ? pageNumber : DEFAULT_PAGE_NUMBER;
    pageSize = pageSize != null ? pageSize : DEFAULT_PAGE_SIZE;
    return PageRequest.of(pageNumber, pageSize);
  }

  @Override
  public Page<Company> getAllCompany(Pageable pageable) {
    return repository.findAll(pageable);
  }

  @Override
  public Company getCompanyById(Integer id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public Company addCompany(Company company) {
    return repository.save(company);
  }

  @Override
  public Company updateCompany(Integer id, Company company) {
    Company existingCompany = repository.findById(id).orElse(null);;
    if (existingCompany == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found");
    }
    existingCompany.setName(company.getName());
    existingCompany.setAddress(company.getAddress());
    existingCompany.setEmail(company.getEmail());
    existingCompany.setPhoneNumber(company.getPhoneNumber());
    existingCompany.setStatus(company.getStatus());
    return repository.save(existingCompany);
  }

  @Override
  public void deleteCompany(Integer id) {
    repository.deleteById(id);
  }
}
