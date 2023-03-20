package com.example.msi.models.student;

import lombok.Data;

@Data
public class UpdateStudentDTO {
  private String code;
  private String majorCode;
  private int status;
  private int teacherId;
  private String grade;
}
