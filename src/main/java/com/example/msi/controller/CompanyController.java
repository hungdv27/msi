package com.example.msi.controller;

import com.example.msi.domains.Company;
import com.example.msi.shared.exceptions.ExceptionUtils;
import com.example.msi.shared.exceptions.MSIException;
import com.example.msi.models.company.CompanyReqDTO;
import com.example.msi.models.company.CreateCompanyDTO;
import com.example.msi.models.company.UpdateCompanyDTO;
import com.example.msi.models.error.ErrorDTO;
import com.example.msi.respone.ImportError;
import com.example.msi.respone.ImportSuccess;
import com.example.msi.service.CompanyService;
import com.example.msi.shared.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

import static com.example.msi.service.impl.CompanyServiceImpl.getPageable;

@RequiredArgsConstructor
@RequestMapping("api/company")
@RestController
@Slf4j
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

  @GetMapping(value = "/template/download", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> templateDownload(HttpServletRequest request) {
    try {
      var bytes = service.templateDownload(request);
      return new ResponseEntity<>(bytes, HttpStatus.OK);
    } catch (Exception ex) {
      log.error(ex.getMessage());
      return new ResponseEntity<>(
          ExceptionUtils.messages.get(ExceptionUtils.E_INTERNAL_SERVER),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping(value = "/import-file", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> importFile(
      @RequestBody MultipartFile file, HttpServletRequest request) {
    try {
      var message = service.importFile(file, request);
      if (message.equals(Constant.IMPORT_SUCCESS)) {
        return new ResponseEntity<>(new ImportSuccess(message), HttpStatus.OK);
      }
      return new ResponseEntity<>(new ImportError(message), HttpStatus.OK);
    } catch (MSIException ex) {
      log.error(ex.getMessage());
      return new ResponseEntity<>(
          new ErrorDTO(ex.getMessageKey(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    } catch (Exception ex) {
      log.error(ex.getMessage());
      return new ResponseEntity<>(
          ExceptionUtils.messages.get(ExceptionUtils.E_INTERNAL_SERVER),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
