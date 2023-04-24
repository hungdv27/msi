package com.example.msi.controller;

import com.example.msi.models.companyresult.CompanyResultDTO;
import com.example.msi.models.companyresult.CreateCompanyResultDTO;
import com.example.msi.service.CompanyResultService;
import com.example.msi.shared.exceptions.MSIException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/company-result")
@RequiredArgsConstructor
public class CompanyResultController {
  private final CompanyResultService service;

  @PostMapping
  public ResponseEntity<CompanyResultDTO> create(
      @RequestPart(value = "dto") CreateCompanyResultDTO dto,
      @RequestPart(value = "files", required = false) List<MultipartFile> files) throws MSIException, IOException {
    dto.setFiles(files);
    var responseData = CompanyResultDTO.getInstance(service.create(dto));
    return new ResponseEntity<>(responseData, HttpStatus.OK);
  }
}
