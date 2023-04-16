package com.example.msi.domains;

import com.example.msi.models.internshipprocess.CreateInternshipProcessDTO;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.NotAudited;
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
@Table(name = "internship_process")
public class InternshipProcess {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "application_id", nullable = false, unique = true)
  private int applicationId;

  @Column(name = "teacher_id")
  private Integer teacherId;

  @Column(name = "created_date", nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdDate;

  @Column(name = "updated_date")
  @LastModifiedDate
  private LocalDateTime updatedDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "application_id", insertable = false, updatable = false, nullable = false)
  @NotAudited
  @Setter(AccessLevel.NONE)
  @Getter(AccessLevel.NONE)
  private InternshipApplication internshipApplication;

  private InternshipProcess(@NonNull CreateInternshipProcessDTO target) {
    applicationId = target.getInternshipApplicationId();
  }

  public static InternshipProcess getInstance(@NonNull CreateInternshipProcessDTO dto) {
    return new InternshipProcess(dto);
  }
}
