package com.example.msi.controller;

import com.example.msi.domains.User;
import com.example.msi.models.user.CreateUserDTO;
import com.example.msi.models.user.LoginUserDTO;
import com.example.msi.models.user.UserDTO;
import com.example.msi.respone.Data;
import com.example.msi.respone.LoginResponse;
import com.example.msi.security.CustomUserDetails;
import com.example.msi.security.jwt.JwtTokenProvider;
import com.example.msi.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@CrossOrigin
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
//  private final SimpMessagingTemplate simpMessagingTemplate;
  private final UserServiceImpl userService;
  private final AuthenticationManager authenticationManager;
  private final JwtTokenProvider tokenProvider;
  private final ModelMapper mapper;

  @PostMapping("/login")
  public ResponseEntity<Data> authenticateUser(@Valid @RequestBody LoginUserDTO user) {
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
      return ResponseEntity.ok(new Data(true, "success", new LoginResponse("Bearer " + jwt,mapper.map(userDetails.getUser(),LoginUserDTO.class))));
    }catch (Exception e){
      throw new IllegalStateException("Sai thông tin đăng nhập");
    }
  }

  @PostMapping("/register")
  public ResponseEntity<Data> registerUser(@Valid @RequestBody CreateUserDTO user, HttpServletRequest request) throws MessagingException, IllegalAccessException {
    return ResponseEntity.ok(userService.register(user, new StringBuffer("")));
  }

}
