package com.example.msi.models.user;

import com.example.msi.domains.User;
import com.example.msi.shared.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
  private int id;

  private String email;

  private Role role;

  private String fullName;

  private LocalDate dob;

  private String phoneNumber;

  private boolean enabled;

  private UserDTO(@NonNull User target) {
    id = target.getId();
    email = target.getEmail();
    role = target.getRole();
    fullName = target.getFullName();
    dob = target.getDob();
    phoneNumber = target.getPhoneNumber();
    enabled = target.isEnabled();
  }

  public static UserDTO getInstance(@NonNull User entity) {
    return new UserDTO(entity);
  }
}
