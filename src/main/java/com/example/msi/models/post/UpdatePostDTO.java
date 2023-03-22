package com.example.msi.models.post;

import com.example.msi.shared.enums.Role;
import lombok.*;


@Getter
public class UpdatePostDTO {
  private int id;
  private String title;
  private Role applyTo;
  private String content;
}
