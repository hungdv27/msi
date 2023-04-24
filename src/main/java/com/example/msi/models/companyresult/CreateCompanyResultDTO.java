package com.example.msi.models.companyresult;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class CreateCompanyResultDTO {
  private Float companyGrade;
  private String companyReview;
  private String studentCode;
  private List<MultipartFile> files;
}
