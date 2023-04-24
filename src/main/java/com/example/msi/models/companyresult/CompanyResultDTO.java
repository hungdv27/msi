package com.example.msi.models.companyresult;

import com.example.msi.domains.CompanyResult;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
public class CompanyResultDTO {
  private Float companyGrade;
  private String companyReview;
  private String studentCode;

  private CompanyResultDTO(@NonNull CompanyResult entity) {
    companyGrade = entity.getCompanyGrade();
    companyReview = entity.getCompanyReview();
    studentCode = entity.getStudentCode();
  }

  public static CompanyResultDTO getInstance(@NonNull CompanyResult entity) {
    return new CompanyResultDTO(entity);
  }
}
