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
@Table(name = "student")
public class Student {
  @Id
  @SequenceGenerator(
      name = "student_id_sequence",
      sequenceName = "student_id_sequence",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_id_sequence")
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
}
