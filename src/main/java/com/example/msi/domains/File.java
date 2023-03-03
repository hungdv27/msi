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
@Table(name = "file")
public class File {
  @Id
  @SequenceGenerator(
      name = "file_id_sequence",
      sequenceName = "file_id_sequence",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "file_id_sequence")
  @Column(name = "id")
  private int id;

  @Column(name = "file_name", nullable = false, length = 255)
  private String fileName;

  @Column(name = "content", nullable = false, length = 255)
  private String content;

  @Column(name = "created_date", nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdDate;

  @Column(name = "updated_date")
  @LastModifiedDate
  private LocalDateTime updatedDate;
}
