package com.example.msi.shared.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public enum Role {
  STUDENT(0), TEACHER(1), ADMIN(2);
  private final int fibonacciNumber;

  Role(int fibonacciNumber) {
    this.fibonacciNumber = fibonacciNumber;
  }

  public int getFibonacciNumber() {
    return fibonacciNumber;
  }
}
