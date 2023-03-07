package com.example.msi.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleEnum {
  STUDENT(0), TEACHER(1), ADMIN(2);
  private final int value;
}
