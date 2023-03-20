package com.example.msi.domains;

import com.example.msi.models.internshipappication.CreateInternshipApplicationDTO;
import com.example.msi.models.internshipappication.UpdateInternshipApplicationDTO;
import com.example.msi.shared.enums.InternshipApplicationStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Table(name = "internship_application")
public class InternshipApplication {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "student_code", length = 50)
  private String studentCode;

  @Column(name = "status", nullable = false)
  @Enumerated
  private InternshipApplicationStatus status;

  @Column(name = "file_id")
  private Integer fileId;

  @Column(name = "company_id")
  private Integer companyId;

  @Column(name = "semester_id", nullable = false)
  private int semesterId;

  @Column(name = "note", length = 10000)
  private String note;

  @Column(name = "created_date", nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdDate;

  @Column(name = "updated_date")
  @LastModifiedDate
  private LocalDateTime updatedDate;

  private InternshipApplication(@NonNull CreateInternshipApplicationDTO target) {
    studentCode = target.getStudentCode();
    status = InternshipApplicationStatus.NEW;
    fileId = target.getFileId();
    companyId = target.getCompanyId();
    semesterId = target.getSemesterId();
    note = target.getNote();
  }

  public void update(@NonNull UpdateInternshipApplicationDTO target) {
    studentCode = target.getStudentCode();
    fileId = target.getFileId();
    companyId = target.getCompanyId();
    semesterId = target.getSemesterId();
  }

  public static InternshipApplication getInstance(@NonNull CreateInternshipApplicationDTO dto) {
    return new InternshipApplication(dto);
  }
}
