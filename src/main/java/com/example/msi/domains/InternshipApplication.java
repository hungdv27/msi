package com.example.msi.domains;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "internship_application")
public class InternshipApplication {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "student_code", length = 50)
  private String code;

  @Column(name = "status", nullable = false)
  private int status;

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
}
