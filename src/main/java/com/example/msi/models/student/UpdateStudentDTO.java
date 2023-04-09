package com.example.msi.models.student;

import lombok.Data;

@Data
public class UpdateStudentDTO {
  private String code;
  private String gradeCode;
  private int status;
}
