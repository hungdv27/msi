package com.example.msi.respone;

import com.example.msi.models.user.LoginUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NotBlank
public class LoginResponse {
  private String jwt;
  private LoginUserDTO user;
}
