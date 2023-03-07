package com.example.msi.models.company;

import lombok.Data;

@Data
public class UpdateSemesterDTO {
  private int id;
  private String semesterName;

  private String startDate;

  private String endDate;
}
