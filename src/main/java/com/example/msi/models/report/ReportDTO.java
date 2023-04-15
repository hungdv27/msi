package com.example.msi.models.report;

import com.example.msi.domains.Report;
import com.example.msi.domains.ReportFile;
import com.example.msi.models.file.FileDTO;
import com.example.msi.service.*;
import com.example.msi.shared.ApplicationContextHolder;
import com.example.msi.shared.utils.Utils;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ReportDTO {
  private final int id;
  private final Integer weekNumber;
  private final int processId;
  private final boolean submitted;
  private final String description;
  private final List<FileDTO> files;
  private final LocalDateTime createdDate;
  private final LocalDateTime updateDate;
  private final boolean onTime;

  private ReportDTO(@NonNull Report target) {
    this.id = target.getId();
    this.weekNumber = target.getWeekNumber();
    this.processId = target.getProcessId();
    this.submitted = target.isSubmitted();
    this.description = target.getDescription();
    this.createdDate = target.getCreatedDate();
    this.updateDate = target.getUpdatedDate();
    var fileIds = ReportDTO.SingletonHelper.REPORT_FILE_SERVICE.findByReportId(target.getId())
        .stream()
        .map(ReportFile::getFileId)
        .collect(Collectors.toList());
    this.files = ReportDTO.SingletonHelper.FILE_SERVICE.findByIds(fileIds).stream().map(FileDTO::getInstance).collect(Collectors.toList());
    // onTime
    var applicationId = SingletonHelper.INTERNSHIP_PROCESS_SERVICE.findById(target.getProcessId()).getApplicationId();
    var internshipApplication = SingletonHelper.INTERNSHIP_APPLICATION_SERVICE.findById(applicationId);
    var checkStartDate = internshipApplication.getStartDate();
    var currentWeekReport = Utils.checkCurrentWeek(checkStartDate, target.getCreatedDate().toLocalDate());
    this.onTime = currentWeekReport == target.getWeekNumber();
  }

  public static ReportDTO getInstance(@NonNull Report entity) {
    return new ReportDTO(entity);
  }

  private static class SingletonHelper {
    private static final ReportFileService REPORT_FILE_SERVICE =
        ApplicationContextHolder.getBean(ReportFileService.class);
    private static final FileService FILE_SERVICE =
        ApplicationContextHolder.getBean(FileService.class);
    private static final InternshipProcessService INTERNSHIP_PROCESS_SERVICE =
        ApplicationContextHolder.getBean(InternshipProcessService.class);
    private static final InternshipApplicationService INTERNSHIP_APPLICATION_SERVICE =
        ApplicationContextHolder.getBean(InternshipApplicationService.class);
  }
}
