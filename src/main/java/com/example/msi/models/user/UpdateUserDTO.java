package com.example.msi.models.user;

import com.example.msi.enums.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDTO {
  private int id;

  private String fullName;

  private LocalDate dob;

  private String phoneNumber;
}
