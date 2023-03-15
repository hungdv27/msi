package com.example.msi.domains;

import com.example.msi.models.semester.CreateSemesterDTO;
import com.example.msi.models.semester.UpdateSemesterDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Table(name = "semester")
public class Semester {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "semester_name", nullable = false, unique = true)
  private String semesterName;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @Column(name = "status")
  private boolean status;

  @Column(name = "created_date", nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdDate;

  @Column(name = "updated_date")
  @LastModifiedDate
  private LocalDateTime updatedDate;

  private Semester(@NonNull CreateSemesterDTO target) {
    this.semesterName = target.getSemesterName();
    this.startDate = LocalDate.parse(target.getStartDate());
    this.endDate = LocalDate.parse(target.getEndDate());
    this.status = false;
  }

  public void update(@NonNull UpdateSemesterDTO target){
    this.semesterName = target.getSemesterName();
    this.startDate = LocalDate.parse(target.getStartDate());
    this.endDate = LocalDate.parse(target.getEndDate());
  }

  public static Semester getInstance(@NonNull CreateSemesterDTO payload){
    return new Semester(payload);
  }
}
