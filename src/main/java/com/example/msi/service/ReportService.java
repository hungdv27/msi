package com.example.msi.service;

import com.example.msi.domains.Report;
import com.example.msi.models.report.CreateReportDTO;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ReportService {
  Report add(@NonNull CreateReportDTO dto, List<MultipartFile> multipartFiles, @NonNull String username) throws Exception;
}
