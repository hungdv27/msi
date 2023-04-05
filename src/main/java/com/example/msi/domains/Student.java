package com.example.msi.domains;

import com.example.msi.models.student.UpdateStudentDTO;
import lombok.*;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Table(name = "student")
public class Student {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "student_code", length = 50, unique = true)
  private String code;

  @Column(name = "major_code", length = 10)
  private String majorCode;

  @Column(name = "user_id")
  private Integer userId;

  @Column(name = "status", nullable = false)
  private int status;

  @Column(name = "grade", nullable = false, length = 10)
  private String grade;

  @Column(name = "created_date", nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdDate;

  @Column(name = "updated_date")
  @LastModifiedDate
  private LocalDateTime updatedDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", insertable = false, updatable = false, nullable = false)
  @NotAudited
  @Setter(AccessLevel.NONE)
  @Getter(AccessLevel.NONE)
  private User user;

  private Student(@NonNull UpdateStudentDTO target, int userId) {
    this.code = target.getCode();
    this.majorCode = target.getMajorCode();
    this.status = target.getStatus();
    this.userId = userId;
    this.grade = target.getGrade();
  }

  public void update(@NonNull UpdateStudentDTO target) {
    this.code = target.getCode();
    this.majorCode = target.getMajorCode();
    this.grade = target.getGrade();
  }

  public static Student getInstance(@NonNull UpdateStudentDTO payload, int userId) {
    return new Student(payload, userId);
  }
}
