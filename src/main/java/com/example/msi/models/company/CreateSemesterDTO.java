package com.example.msi.models.company;

import lombok.Data;

@Data
public class CreateSemesterDTO {
  private String name;
  private String startDate;
  private String endDate;
}
