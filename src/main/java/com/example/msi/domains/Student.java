package com.example.msi.domains;

import com.example.msi.models.student.UpdateStudentDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
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

  @Column(name = "teacher_id", nullable = false)
  private int teacherId;

  @Column(name = "grade", nullable = false, length = 10)
  private String grade;

  @Column(name = "created_date", nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdDate;

  @Column(name = "updated_date")
  @LastModifiedDate
  private LocalDateTime updatedDate;

  private Student(@NonNull UpdateStudentDTO target, int userId) {
    this.code = target.getCode();
    this.majorCode = target.getMajorCode();
    this.status = target.getStatus();
    this.teacherId = target.getTeacherId();
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
