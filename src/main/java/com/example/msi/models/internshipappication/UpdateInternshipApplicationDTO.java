package com.example.msi.models.internshipappication;

import lombok.Getter;


@Getter
public class UpdateInternshipApplicationDTO {
  private int id;
  private String studentCode;
  private Integer fileId;
  private Integer companyId;
  private int semesterId;
}
