package com.example.msi.models.courses;

import lombok.Data;

@Data
public class UpdateCoursesDTO {
  private int id;

  private String code;

  private String name;

  private String description;
}
