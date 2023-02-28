package com.example.msi.domains;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "internship_process")
public class InternshipProcess {
  @Id
  @SequenceGenerator(
      name = "internship_process_id_sequence",
      sequenceName = "internship_process_id_sequence",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "internship_process_id_sequence")
  @Column(name = "id")
  private int id;

  @Column(name = "application_id", nullable = false, unique = true)
  private int applicationId;

  @Column(name = "teacher_id")
  private int teacherId;
  @Column(name = "report_id")
  private int reportId;

  @Column(name = "created_date", nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdDate;

  @Column(name = "updated_date")
  @LastModifiedDate
  private LocalDateTime updatedDate;}
