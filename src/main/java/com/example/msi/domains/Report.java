package com.example.msi.domains;

import com.example.msi.models.report.CreateReportDTO;
import com.example.msi.models.report.ReportDescriptionDTO;
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
@Table(name = "report")
public class Report {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "week_number")
  private Integer weekNumber;

  @Column(name = "process_id", nullable = false)
  private int processId;

  @Column(name = "submitted", nullable = false)
  private boolean submitted;

  @Column(name = "description", length = 255)
  private String description;

  @Column(name = "created_date", nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdDate;

  @Column(name = "updated_date")
  @LastModifiedDate
  private LocalDateTime updatedDate;

  private Report(@NonNull CreateReportDTO target, int processId) {
    weekNumber = target.getWeekNumber();
    submitted = false;
    this.processId = processId;
  }

  public static Report getInstance(@NonNull CreateReportDTO dto, int processId) {
    return new Report(dto, processId);
  }

  public void addDescription(@NonNull ReportDescriptionDTO target) {
    this.description = target.getDescription();
  }
}
