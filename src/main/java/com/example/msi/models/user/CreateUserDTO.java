package com.example.msi.models.user;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateUserDTO {
  private String email;
  private String password;
  private String fullName;
  private LocalDate dob;
  private String phoneNumber;
}
