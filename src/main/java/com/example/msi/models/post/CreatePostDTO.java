package com.example.msi.models.post;

import com.example.msi.enums.PostApplyTo;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
public class CreatePostDTO {
  private String title;
  private Set<PostApplyTo> applyTo;
  private String content;
}
