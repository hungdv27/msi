package com.example.msi.models.semester;

import lombok.Data;

@Data
public class CreateSemesterDTO {
  private String semesterName;
  private String startDate;
  private String endDate;
}
