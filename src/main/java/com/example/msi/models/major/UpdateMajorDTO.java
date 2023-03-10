package com.example.msi.models.major;

import lombok.Data;

@Data
public class UpdateMajorDTO {
  private int id;

  private String code;

  private String name;

  private String description;
}
