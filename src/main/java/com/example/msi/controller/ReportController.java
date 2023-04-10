package com.example.msi.controller;

import com.example.msi.models.report.CreateReportDTO;
import com.example.msi.models.report.ReportDTO;
import com.example.msi.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("api/report")
@RestController
public class ReportController {
  private final ReportService service;

  @PostMapping
  public ResponseEntity<ReportDTO> createReport(
      @RequestPart(value = "body") CreateReportDTO dto,
      @RequestPart(value = "files", required = false) List<MultipartFile> files,
      Principal principal
  ) throws Exception {
    var response = ReportDTO.getInstance(service.add(dto, files, principal.getName()));
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
