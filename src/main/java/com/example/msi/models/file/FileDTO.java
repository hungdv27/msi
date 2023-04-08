package com.example.msi.models.file;

import com.example.msi.domains.FileE;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
public class FileDTO {
  private String fileName;
  private String fileKey;

  private FileDTO(@NonNull FileE target) {
    fileKey = target.getFileKey();
    fileName = target.getFilename();
  }

  public static FileDTO getInstance(@NonNull FileE entity) {
    return new FileDTO(entity);
  }
}
