package com.example.msi.models.post;

import com.example.msi.shared.enums.Role;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Getter
@Setter
public class UpdatePostDTO {
  private int id;
  private String title;
  private Role applyTo;
  private String content;
  private List<MultipartFile> files;
}
