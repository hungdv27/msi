package com.example.msi.models.report;

import com.example.msi.domains.Report;
import com.example.msi.domains.ReportFile;
import com.example.msi.models.file.FileDTO;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.msi.shared.utils.ServiceUtils.*;
import static com.example.msi.shared.utils.Utils.checkCurrentWeek;

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
    var fileIds = getReportFileService()
        .findByReportId(target.getId())
        .stream()
        .map(ReportFile::getFileId)
        .collect(Collectors.toList());
    this.files = getFileService()
        .findByIds(fileIds).stream().map(FileDTO::getInstance).collect(Collectors.toList());
    // onTime
    var applicationId = getInternshipProcessService()
        .findById(target.getProcessId()).orElseThrow().getApplicationId();
    var internshipApplication = getInternshipApplicationService()
        .findById(applicationId);
    var checkStartDate = internshipApplication.getStartDate();
    var currentWeekReport = checkCurrentWeek(checkStartDate, target.getCreatedDate().toLocalDate());
    this.onTime = currentWeekReport == target.getWeekNumber();
  }

  public static ReportDTO getInstance(@NonNull Report entity) {
    return new ReportDTO(entity);
  }
}
