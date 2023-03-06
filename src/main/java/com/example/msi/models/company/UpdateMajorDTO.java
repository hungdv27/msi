package com.example.msi.models.company;

import lombok.Data;

@Data
public class UpdateMajorDTO {
  private int id;

  private String code;

  private String name;

  private String description;
}
