package com.example.msi.controller;

import com.example.msi.domains.Company;
import com.example.msi.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import static com.example.msi.service.impl.CompanyServiceImpl.getPageable;

@RequiredArgsConstructor
@RequestMapping("/company")
@RestController
public class CompanyController {
  private final CompanyService service;

  @GetMapping("/get-all")
  public ResponseEntity<Page<Company>> getAllCompany(
      @RequestParam(defaultValue = "0") Integer pageNumber,
      @RequestParam(defaultValue = "10") Integer pageSize
  ) {
    Pageable pageable = getPageable(pageNumber, pageSize);
    Page<Company> page = service.getAllCompany(pageable);
    return ResponseEntity.ok(page);
  }

  @PostMapping("/create")
  public Company createCompany(@RequestBody Company company) {
    return service.addCompany(company);
  }

  @PutMapping("/{id}")
  public Company updateCompany(@PathVariable Integer id, @RequestBody Company company) {
    return service.updateCompany(id, company);
  }

  @DeleteMapping("delete/{id}")
  public void deleteBook(@PathVariable Integer id) {
    service.deleteCompany(id);
  }

}
