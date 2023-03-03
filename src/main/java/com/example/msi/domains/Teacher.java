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
@Table(name = "teacher")
public class Teacher {
  @Id
  @SequenceGenerator(
      name = "teacher_id_sequence",
      sequenceName = "teacher_id_sequence",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "teacher_id_sequence")
  @Column(name = "id")
  private int id;

  @Column(name = "user_id")
  private Integer userId;

  @Column(name = "created_date", nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdDate;

  @Column(name = "updated_date")
  @LastModifiedDate
  private LocalDateTime updatedDate;
}
