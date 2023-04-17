package com.example.msi.domains;

import com.example.msi.models.result.CreateResultDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.example.msi.shared.utils.ServiceUtils.getInternshipApplicationService;
import static com.example.msi.shared.utils.ServiceUtils.getInternshipProcessService;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Table(name = "result")
public class Result {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "process_id", nullable = false)
  private int processId;

  @Column(name = "mark")
  private int mark;

  @Column(name = "review", length = 1000)
  private String review;

  @Column(name = "student_code", length = 50)
  private String studentCode;

  @Column(name = "created_date", nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdDate;

  @Column(name = "updated_date")
  @LastModifiedDate
  private LocalDateTime updatedDate;

  private Result(@NonNull CreateResultDTO target) {
    processId = target.getProcessId();
    mark = target.getMark();
    review = target.getReview();
    var applicationId = getInternshipProcessService().findById(processId).orElseThrow().getApplicationId();
    studentCode = getInternshipApplicationService().findById(applicationId).getStudentCode();
  }

  public static Result getInstance(@NonNull CreateResultDTO dto) {
    return new Result(dto);
  }
}
