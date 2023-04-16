package com.example.msi.models.internshipprocess;

import lombok.Data;

import java.util.List;

@Data
public class AssignTeacherDTO {
  private int teacherId;
  private List<Integer> applicationId;
}
