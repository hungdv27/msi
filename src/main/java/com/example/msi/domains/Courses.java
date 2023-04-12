package com.example.msi.domains;

import com.example.msi.models.courses.CreateCoursesDTO;
import com.example.msi.models.courses.UpdateCoursesDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Table(name = "courses")
public class Courses {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "code", length = 10)
  private String code;

  @Column(name = "name", length = 255)
  private String name;

  @Column(name = "description", length = 255)
  private String description;

  @Column(name = "created_date", nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdDate;

  @Column(name = "updated_date")
  @LastModifiedDate
  private LocalDateTime updatedDate;

  private Courses(@NonNull CreateCoursesDTO target) {
    this.code = target.getCode();
    this.name = target.getName();
    this.description = target.getDescription();
  }

  public void update(@NonNull UpdateCoursesDTO target){
    this.code = target.getCode();
    this.name = target.getName();
    this.description = target.getDescription();
  }

  public static Courses getInstance(@NonNull CreateCoursesDTO payload){
    return new Courses(payload);
  }
}
