package com.example.msi.models.post;

import com.example.msi.shared.enums.PostApplyTo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreatePostDTO {
  private String title;
  private PostApplyTo applyTo;
  private String content;
}
