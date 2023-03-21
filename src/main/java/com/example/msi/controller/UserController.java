package com.example.msi.controller;

import com.example.msi.domains.User;
import com.example.msi.models.user.*;
import com.example.msi.repository.UserRepository;
import com.example.msi.response.Data;
import com.example.msi.response.LoginResponse;
import com.example.msi.security.CustomUserDetails;
import com.example.msi.security.jwt.JwtTokenProvider;
import com.example.msi.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {
  private final UserServiceImpl service;
  private final UserRepository repository;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider tokenProvider;
  private final ModelMapper mapper;

  @PostMapping("/login")
  public ResponseEntity<Data> authenticateUser(@Valid @RequestBody LoginUserDTO user) {
    Optional<User> userOptional = repository.findByEmail(user.getEmail());
    if (userOptional.isPresent() && !userOptional.get().isEnabled()) {
      throw new IllegalStateException("Tài khoản chưa được kích hoạt");
    }
    // Xác thực thông tin người dùng Request lên
    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
              user.getEmail(),
              user.getPassword()
          )
      );
      // Nếu không xảy ra exception tức là thông tin hợp lệ
      // Set thông tin authentication vào Security Context
      SecurityContextHolder.getContext().setAuthentication(authentication);
      // Trả về jwt cho người dùng.
      CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
      String jwt = tokenProvider.generateToken(userDetails);
      return ResponseEntity.ok(new Data(new LoginResponse("Bearer " + jwt, mapper.map(userDetails.getUser(), LoginUserDTO.class))));
    } catch (Exception e) {
      throw new IllegalStateException("Sai thông tin đăng nhập");
    }

  }

  @PostMapping("/register")
  public ResponseEntity<Data> registerUser(@Valid @RequestBody CreateUserDTO user, HttpServletRequest request) throws MessagingException, IllegalAccessException {
    return ResponseEntity.ok(service.register(user, request.getRequestURL()));
  }

  @GetMapping("/register/verify")
  public ResponseEntity<Data> verifyUser(@RequestParam("code") String code) throws IllegalAccessException {
    return ResponseEntity.ok(service.verify(code));
  }

  @PostMapping("/update-password")
  public ResponseEntity<Data> updatePassword(@RequestBody UpdatePasswordUserDTO updatePasswordUser) throws IOException {
    return ResponseEntity.ok(service.updatePassword(updatePasswordUser));
  }

  @GetMapping("/forgot-password")
  public ResponseEntity<Data> forgotPassword(@RequestParam String mail) throws MessagingException {
    return ResponseEntity.ok(service.forgotPassword(mail));
  }

  @GetMapping("/user_access_information")
  public ResponseEntity<Data> userAccessInformation() throws IllegalAccessException {
    Optional<User> user = service.userAccessInformation();
    return ResponseEntity.ok(new Data(user));
  }

  @PutMapping("/update")
  public ResponseEntity<Data> updateUser(@RequestBody UpdateUserDTO updateUser) throws IOException {
    return ResponseEntity.ok(new Data(service.update(updateUser)));
  }
}
