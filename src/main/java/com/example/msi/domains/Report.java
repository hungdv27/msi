package com.example.msi.domains;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "report")
public class Report {
  @Id
  @SequenceGenerator(
      name = "report_id_sequence",
      sequenceName = "report_id_sequence",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "report_id_sequence")
  @Column(name = "id")
  private int id;

  @Column(name = "week_number", nullable = false)
  private int weekNumber;

  @Column(name = "file_id")
  private Integer fileId;

  @Column(name = "submitted",nullable = false)
  private boolean submitted;

  @Column(name = "description", length = 255)
  private String description;

  @Column(name = "created_date", nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdDate;

  @Column(name = "updated_date")
  @LastModifiedDate
  private LocalDateTime updatedDate;
}
