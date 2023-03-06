package com.example.msi.service.impl;

import com.example.msi.domains.Company;
import com.example.msi.models.company.CreateCompanyDTO;
import com.example.msi.models.company.UpdateCompanyDTO;
import com.example.msi.repository.CompanyRepository;
import com.example.msi.service.CompanyService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
  private final CompanyRepository repository;

  private static final int DEFAULT_PAGE_NUMBER = 0;
  private static final int DEFAULT_PAGE_SIZE = 10;

  public static Pageable getPageable(Integer page, Integer size) {
    page = page != null ? page : DEFAULT_PAGE_NUMBER;
    size = size != null ? size : DEFAULT_PAGE_SIZE;
    return PageRequest.of(page, size);
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
  public Company addCompany(CreateCompanyDTO payload) {
    var entity = Company.getInstance(payload);
    return repository.save(entity);
  }

  @Override
  public Optional<Company> updateCompany(@NonNull UpdateCompanyDTO payload) {
    var id=payload.getId();
    return repository.findById(id).map(e ->{
      e.update(payload);
      return repository.save(e);
    });
  }

  @Override
  public List<Company> searchCompanyByName(@NonNull String name) {
    return repository.getCompanyByName(name);
  }

  @Override
  public void deleteCompany(Integer id) {
    repository.deleteById(id);
  }
}
