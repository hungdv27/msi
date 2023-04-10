package com.example.msi.models.reportfile;

import lombok.Getter;

@Getter
public class CreateReportFileDTO {
  private int reportId;
  private int fileId;

  private CreateReportFileDTO(int reportId, int fileId) {
    this.reportId = reportId;
    this.fileId = fileId;
  }

  public static CreateReportFileDTO getInstance(int reportId, int fileId) {
    return new CreateReportFileDTO(reportId, fileId);
  }
}
