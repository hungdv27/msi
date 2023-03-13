package com.example.msi.controller;

import com.example.msi.domains.Company;
import com.example.msi.models.company.CreateCompanyDTO;
import com.example.msi.models.company.UpdateCompanyDTO;
import com.example.msi.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

import static com.example.msi.service.impl.CompanyServiceImpl.getPageable;

@RequiredArgsConstructor
@RequestMapping("api/company")
@RestController
public class CompanyController {
  private final CompanyService service;

  @GetMapping("")
  public ResponseEntity<Page<Company>> getAllCompany(
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "10") Integer size
  ) {
    Pageable pageable = getPageable(page, size);
    Page<Company> page1 = service.getAllCompany(pageable);
    return ResponseEntity.ok(page1);
  }

  @PostMapping("")
  public Company createCompany(@RequestBody CreateCompanyDTO company) {
    return service.addCompany(company);
  }

  @PutMapping("")
  public Optional<Company> updateCompany(@RequestBody UpdateCompanyDTO payload) {
    return service.updateCompany(payload);
  }

  @DeleteMapping("/{id}")
  public void deleteCompany(@PathVariable Integer id) {
    service.deleteCompany(id);
  }

  @GetMapping("/{name}")
  public List<Company> getCompanyByName(@PathVariable String name){
    return service.searchCompanyByName(name);
  }

}
