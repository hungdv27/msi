package com.example.msi.models.report;

import com.example.msi.domains.Post;
import com.example.msi.domains.PostFile;
import com.example.msi.domains.Report;
import com.example.msi.domains.ReportFile;
import com.example.msi.models.file.FileDTO;
import com.example.msi.models.post.PostDTO;
import com.example.msi.service.FileService;
import com.example.msi.service.PostFileService;
import com.example.msi.service.ReportFileService;
import com.example.msi.shared.ApplicationContextHolder;
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
  }

  public static ReportDTO getInstance(@NonNull Report entity) {
    return new ReportDTO(entity);
  }

  private static class SingletonHelper {
    private static final ReportFileService REPORT_FILE_SERVICE =
        ApplicationContextHolder.getBean(ReportFileService.class);

    private static final FileService FILE_SERVICE =
        ApplicationContextHolder.getBean(FileService.class);
  }
}
