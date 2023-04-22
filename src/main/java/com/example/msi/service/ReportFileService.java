package com.example.msi.service;

import com.example.msi.domains.ReportFile;
import com.example.msi.models.reportfile.CreateReportFileDTO;
import org.springframework.lang.NonNull;

import java.util.List;

public interface ReportFileService {
  void add(@NonNull CreateReportFileDTO dto);

  List<ReportFile> findByReportId(@NonNull Integer reportId);

  void deleteByReportId(int reportId);
}
