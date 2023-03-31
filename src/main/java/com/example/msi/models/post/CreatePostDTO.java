package com.example.msi.models.post;

import com.example.msi.shared.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreatePostDTO {
  private String title;
  private Role applyTo;
  private String content;
}
