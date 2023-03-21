package com.example.msi.domains;


import com.example.msi.models.company.CreateCompanyDTO;
import com.example.msi.models.file.CreateFileDTO;
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
@Table(name = "file")
public class File {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "file_name", nullable = false, length = 255)
  private String fileName;

  @Column(name = "created_date", nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdDate;

  @Column(name = "updated_date")
  @LastModifiedDate
  private LocalDateTime updatedDate;

  @Column(name = "file_url", nullable = false, length = 5000)
  private String fileURL;

  @Column(name = "public_file_id")
  private String publicFileId;

  public File(@NonNull CreateFileDTO target) {
    this.fileName = target.getFileName();
  }

  public static File getInstance(@NonNull CreateFileDTO payload) {
    return new File(payload);
  }
}
