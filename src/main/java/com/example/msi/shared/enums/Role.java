package com.example.msi.shared.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {
  STUDENT(0), TEACHER(1), ADMIN(2);
  private final int value;
}