package com.example.msi.models.post;

import com.example.msi.shared.enums.PostApplyTo;
import com.example.msi.shared.enums.Role;
import lombok.Getter;

import java.util.Set;

@Getter
public class CreatePostDTO {
  private String title;
  private Set<Role> applyTo;
  private String content;
}
