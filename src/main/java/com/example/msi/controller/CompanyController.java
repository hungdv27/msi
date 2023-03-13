package com.example.msi.controller;

import com.example.msi.domains.Company;
import com.example.msi.exceptions.ExceptionUtils;
import com.example.msi.exceptions.MSIException;
import com.example.msi.models.company.CompanyReqDTO;
import com.example.msi.models.company.CreateCompanyDTO;
import com.example.msi.models.company.UpdateCompanyDTO;
import com.example.msi.models.error.ErrorDTO;
import com.example.msi.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
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

  @GetMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> exportListCompany(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
      @RequestParam(value = "email", required = false) String email,
      @RequestParam(value = "address", required = false) String address,
      @RequestParam(value = "status", required = false) String status,
      HttpServletRequest request) {
    byte[] bytes;
    try {
      CompanyReqDTO reqDTO = new CompanyReqDTO();
      reqDTO.setName(name);
      reqDTO.setPhoneNumber(phoneNumber);
      reqDTO.setEmail(email);
      reqDTO.setAddress(address);
      reqDTO.setStatus(status);
      bytes = (byte[]) service.export(request, reqDTO);
      return new ResponseEntity<>(bytes, HttpStatus.OK);
    } catch (MSIException ex) {
      return new ResponseEntity<>(
          new ErrorDTO(ex.getMessageKey(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    } catch (Exception ex) {
      return new ResponseEntity<>(
          ExceptionUtils.messages.get(ExceptionUtils.E_INTERNAL_SERVER),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
