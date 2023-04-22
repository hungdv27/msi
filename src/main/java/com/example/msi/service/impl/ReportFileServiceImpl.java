package com.example.msi.service.impl;

import com.example.msi.domains.ReportFile;
import com.example.msi.models.reportfile.CreateReportFileDTO;
import com.example.msi.repository.ReportFileRepository;
import com.example.msi.service.ReportFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportFileServiceImpl implements ReportFileService {
  private final ReportFileRepository repository;

  @Override
  public void add(@NonNull CreateReportFileDTO dto) {
    var entity = ReportFile.getInstance(dto);
    repository.save(entity);
  }

  @Override
  public List<ReportFile> findByReportId(@NonNull Integer reportId) {
    return repository.findAllByReportId(reportId);
  }

  @Override
  @Transactional
  public void deleteByReportId(int reportId) {
    repository.deleteAllByReportId(reportId);
  }
}
