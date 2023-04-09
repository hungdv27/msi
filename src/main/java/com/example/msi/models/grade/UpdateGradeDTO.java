package com.example.msi.models.grade;

import lombok.Data;

@Data
public class UpdateGradeDTO {
  private int id;

  private String code;

  private String name;

  private String description;
}
