package com.example.msi.domains;

import com.example.msi.models.companyresult.CreateCompanyResultDTO;
import com.example.msi.models.companyresult.IncomeCompanyResultCreateDTO;
import com.example.msi.shared.exceptions.MSIException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Table(name = "company_result")
public class CompanyResult {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "student_code", length = 50)
  private String studentCode;

  @Column(name = "company_grade")
  private float companyGrade;

  @Column(name = "company_review", length = 1000)
  private String companyReview;

  @Column(name = "created_date", nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdDate;

  @Column(name = "updated_date")
  @LastModifiedDate
  private LocalDateTime updatedDate;

  private CompanyResult(@NonNull CreateCompanyResultDTO target) throws MSIException {
    studentCode = target.getStudentCode();
    companyGrade = target.getCompanyGrade();
    companyReview = target.getCompanyReview();
  }

  public CompanyResult(@NonNull IncomeCompanyResultCreateDTO target) {
    this.studentCode = target.getStudentCode();
    this.companyGrade = Float.parseFloat(target.getCompanyGrade());
    this.companyReview = target.getCompanyReview();
  }

  public static CompanyResult getInstance(@NonNull CreateCompanyResultDTO dto) throws MSIException {
    return new CompanyResult(dto);
  }
}
