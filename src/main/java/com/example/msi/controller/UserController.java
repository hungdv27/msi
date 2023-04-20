package com.example.msi.controller;

import com.example.msi.domains.User;
import com.example.msi.models.error.ErrorDTO;
import com.example.msi.models.user.*;
import com.example.msi.repository.UserRepository;
import com.example.msi.response.Data;
import com.example.msi.response.ImportError;
import com.example.msi.response.ImportSuccess;
import com.example.msi.response.LoginResponse;
import com.example.msi.security.CustomUserDetails;
import com.example.msi.security.jwt.JwtTokenProvider;
import com.example.msi.service.impl.UserServiceImpl;
import com.example.msi.shared.Constant;
import com.example.msi.shared.exceptions.ExceptionUtils;
import com.example.msi.shared.exceptions.MSIException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;


@CrossOrigin
@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
@Slf4j
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
      return ResponseEntity.ok(new Data(new LoginResponse("Bearer " + jwt, new Date((new Date()).getTime() + 30000).getTime(), mapper.map(userDetails.getUser(), LoginUserDTO.class))));
    } catch (Exception e) {
      throw new ResponseStatusException(
          HttpStatus.BAD_REQUEST,"Sai thông tin đăng nhập", e
      );
    }

  }

  @PostMapping("/register")
  public ResponseEntity<Data> registerUser(@Valid @RequestBody CreateUserDTO user, HttpServletRequest request) throws MessagingException, IllegalAccessException {
    return ResponseEntity.ok(service.register(user, request.getRequestURL()));
  }

  @PostMapping("/register-teacher")
  public ResponseEntity<Data> registerTeacherAccount(@Valid @RequestBody CreateUserDTO user, HttpServletRequest request) throws MessagingException, IllegalAccessException {
    return ResponseEntity.ok(service.registerTeacherAccount(user, request.getRequestURL()));
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

  @GetMapping("/user-access-information")
  public ResponseEntity<Data> userAccessInformation() throws IllegalAccessException {
    Optional<User> user = service.userAccessInformation();
    return ResponseEntity.ok(new Data(user));
  }

  @PutMapping("/update")
  public ResponseEntity<Data> updateUser(@RequestBody UpdateUserDTO updateUser) throws IOException {
    return ResponseEntity.ok(new Data(service.update(updateUser)));
  }

  @GetMapping("/find-all")
  public ResponseEntity<Page<UserDTO>> search(
      SearchUserDTO searchDTO
  ) {
    var responseData = service.findAll(searchDTO)
        .map(UserDTO::getInstance);
    return new ResponseEntity<>(responseData, HttpStatus.OK);
  }

  @PostMapping("/change-enable/{userId}")
  public ResponseEntity<Data> changeEnable(@PathVariable Integer userId) {
    var responseData = service.changeEnable(userId).map(UserDTO::getInstance);
    return ResponseEntity.ok(new Data(responseData));
  }

  @GetMapping(value = "/template/download", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> templateDownload(HttpServletRequest request) {
    try {
      var bytes = service.templateDownload(request);
      return new ResponseEntity<>(bytes, HttpStatus.OK);
    } catch (Exception ex) {
      log.error(ex.getMessage());
      return new ResponseEntity<>(
          ExceptionUtils.messages.get(ExceptionUtils.E_INTERNAL_SERVER),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping(value = "/import-file", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> importFile(
      @RequestBody MultipartFile file, HttpServletRequest request) {
    try {
      var message = service.importFile(file, request);
      if (message.equals(Constant.IMPORT_SUCCESS)) {
        return new ResponseEntity<>(new ImportSuccess(message), HttpStatus.OK);
      }
      return new ResponseEntity<>(new ImportError(message), HttpStatus.OK);
    } catch (MSIException ex) {
      log.error(ex.getMessage());
      return new ResponseEntity<>(
          new ErrorDTO(ex.getMessageKey(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    } catch (Exception ex) {
      log.error(ex.getMessage());
      return new ResponseEntity<>(
          ExceptionUtils.messages.get(ExceptionUtils.E_INTERNAL_SERVER),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
