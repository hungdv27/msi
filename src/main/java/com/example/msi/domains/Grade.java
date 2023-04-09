package com.example.msi.domains;

import com.example.msi.models.grade.CreateGradeDTO;
import com.example.msi.models.grade.UpdateGradeDTO;
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
@Table(name = "grade")
public class Grade {
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

  private Grade(@NonNull CreateGradeDTO target) {
    this.code = target.getCode();
    this.name = target.getName();
    this.description = target.getDescription();
  }

  public void update(@NonNull UpdateGradeDTO target){
    this.code = target.getCode();
    this.name = target.getName();
    this.description = target.getDescription();
  }

  public static Grade getInstance(@NonNull CreateGradeDTO payload){
    return new Grade(payload);
  }
}
