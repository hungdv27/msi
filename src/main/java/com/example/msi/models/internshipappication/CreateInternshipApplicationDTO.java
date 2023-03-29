package com.example.msi.models.internshipappication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
public class CreateInternshipApplicationDTO {
  private Integer companyId;
  private String note;
  private String username;
  private List<MultipartFile> files;
}
