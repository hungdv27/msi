package com.example.msi.models.post;

import com.example.msi.shared.enums.Role;
import lombok.Getter;

@Getter
public class CreatePostDTO {
  private String title;
  private Role applyTo;
  private String content;
}
