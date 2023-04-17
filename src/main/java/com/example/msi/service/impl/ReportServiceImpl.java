package com.example.msi.service.impl;

import com.example.msi.domains.FileE;
import com.example.msi.domains.Report;
import com.example.msi.models.report.CreateReportDTO;
import com.example.msi.models.reportfile.CreateReportFileDTO;
import com.example.msi.repository.ReportRepository;
import com.example.msi.service.*;
import com.example.msi.shared.enums.InternshipApplicationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
  private final ReportRepository repository;
  private final StudentService studentService;
  private final InternshipApplicationService internshipApplicationService;
  private final InternshipProcessService internshipProcessService;
  private final FileService fileService;
  private final ReportFileService reportFileService;

  @Override
  @Transactional
  public Report add(@NonNull CreateReportDTO dto, List<MultipartFile> multipartFiles, @NonNull String username) throws Exception {
    var studentCode = studentService.findByUsername(username).orElseThrow().getCode();
    var applicationId = internshipApplicationService.findByStudentCodeAndStatus(studentCode, InternshipApplicationStatus.ACCEPTED)
        .orElseThrow().getId();
    var processId = internshipProcessService.findByApplicationId(applicationId).orElseThrow().getId();
    var report = repository.save(Report.getInstance(dto, processId));
    attachFiles(report.getId(), multipartFiles);
    return report;
  }

  @Override
  public Report findById(int id) {
    return repository.findById(id).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public List<Report> findAllByProcessId(int processId) {
    return repository.findAllByProcessId(processId);
  }

  private void attachFiles(int reportId, List<MultipartFile> multipartFiles) throws IOException {
    if (multipartFiles == null) return;
    var files = fileService.uploadFiles(multipartFiles);
    for (FileE file : files) {
      var pf = CreateReportFileDTO.getInstance(reportId, file.getId());
      reportFileService.add(pf);
    }
  }
}
