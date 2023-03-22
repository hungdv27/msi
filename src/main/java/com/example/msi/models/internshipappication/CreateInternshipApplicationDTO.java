package com.example.msi.models.internshipappication;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateInternshipApplicationDTO {
  private Integer fileId;
  private Integer companyId;
  private String note;
  private String username;
}
