package com.example.msi.controller;

import com.example.msi.models.report.CreateReportDTO;
import com.example.msi.models.report.ReportDTO;
import com.example.msi.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

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

  @GetMapping("/{id}")
  public ResponseEntity<ReportDTO> reportById(@PathVariable int id) throws NoSuchElementException {
    var response = ReportDTO.getInstance(service.findById(id));
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
