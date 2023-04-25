package com.example.msi.models.post;

import com.example.msi.shared.enums.PostApplyTo;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Getter
@Setter
public class UpdatePostDTO {
  private int id;
  private String title;
  private PostApplyTo applyTo;
  private String content;
  private List<String> existedFiles;
  private List<MultipartFile> fileNews;
}
