package com.example.msi.models.companyresult_file;

import lombok.Getter;

@Getter
public class CreateCompanyResultFileDTO {
  private int companyResultId;
  private int fileId;

  private CreateCompanyResultFileDTO(int companyResultId, int fileId) {
    this.companyResultId = companyResultId;
    this.fileId = fileId;
  }

  public static CreateCompanyResultFileDTO getInstance(int companyResultId, int fileId) {
    return new CreateCompanyResultFileDTO(companyResultId, fileId);
  }
}
