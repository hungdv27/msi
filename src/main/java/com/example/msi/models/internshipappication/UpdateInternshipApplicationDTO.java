package com.example.msi.models.internshipappication;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@AllArgsConstructor
public class UpdateInternshipApplicationDTO {
  private int id;
  private Integer companyId;
  private List<MultipartFile> files;
}
