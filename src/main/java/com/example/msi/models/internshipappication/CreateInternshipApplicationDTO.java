package com.example.msi.models.internshipappication;

import lombok.Getter;

@Getter
public class CreateInternshipApplicationDTO {
  private String studentCode;
  private Integer fileId;
  private Integer companyId;
  private int semesterId;
  private String note;
}
