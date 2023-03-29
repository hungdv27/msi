package com.example.msi.models.internshipapplication_file;


import lombok.Getter;

@Getter
public class CreateInternshipApplicationFileDTO {
  private int internshipApplicationId;
  private int fileId;

  private CreateInternshipApplicationFileDTO(int internshipApplicationId, int fileId) {
    this.internshipApplicationId = internshipApplicationId;
    this.fileId = fileId;
  }

  public static CreateInternshipApplicationFileDTO getInstance(int internshipApplicationId, int fileId) {
    return new CreateInternshipApplicationFileDTO(internshipApplicationId, fileId);
  }
}
