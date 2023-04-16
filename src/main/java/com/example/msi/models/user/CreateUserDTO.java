package com.example.msi.models.user;

import com.example.msi.domains.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class CreateUserDTO {
  private String email;
  private String password;
  private String fullName;
  private LocalDate dob;
  private String phoneNumber;

  private CreateUserDTO(@NonNull User user) {
    email = user.getEmail();
    password = user.getPassword();
    fullName = user.getFullName();
    dob = user.getDob();
    phoneNumber = user.getPhoneNumber();
  }

  public static CreateUserDTO getInstance(@NonNull User entity) {
    return new CreateUserDTO(entity);
  }
}
