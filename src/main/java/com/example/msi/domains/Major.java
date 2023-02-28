package com.example.msi.domains;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "major")
public class Major {
  @Id
  @SequenceGenerator(
      name = "major_id_sequence",
      sequenceName = "major_id_sequence",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "major_id_sequence")
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
}
