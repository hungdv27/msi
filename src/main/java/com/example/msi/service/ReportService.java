package com.example.msi.service;

import com.example.msi.domains.Report;
import com.example.msi.models.report.CreateReportDTO;
import com.example.msi.models.report.ReportDescriptionDTO;
import com.example.msi.shared.exceptions.MSIException;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReportService {
  Report add(@NonNull CreateReportDTO dto, List<MultipartFile> multipartFiles, @NonNull String username) throws Exception;

  Report findById(int id);

  List<Report> findAllByProcessId(int processId);

  void addDescription(@NonNull ReportDescriptionDTO payload) throws MSIException;

  void delete(int id);
}
