package com.example.msi.models.post;

import com.example.msi.enums.PostApplyTo;
import lombok.Getter;

import java.util.Set;

@Getter
public class UpdatePostDTO {
  private int id;
  private String title;
  private Set<PostApplyTo> applyTo;
  private String content;
}
