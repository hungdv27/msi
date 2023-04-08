package com.example.msi.models.file;

import com.example.msi.domains.FileE;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Getter
public class FileDTO {
  private String fileName;
  private String fileKey;
  private Long size;
  private LocalDateTime createdDate;


  private FileDTO(@NonNull FileE target) {
    fileKey = target.getFileKey();
    fileName = target.getFilename();
    size = target.getSize();
    createdDate = target.getCreatedDate();
  }

  public static FileDTO getInstance(@NonNull FileE entity) {
    return new FileDTO(entity);
  }
}
