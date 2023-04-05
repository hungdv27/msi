package com.example.msi.models.internshipappication;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class UpdateInternshipApplicationDTO {
  private int id;
  private Integer companyId;
  private List<MultipartFile> files;
}
