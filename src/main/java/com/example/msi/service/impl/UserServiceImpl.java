package com.example.msi.service.impl;

import com.example.msi.domains.User;
import com.example.msi.enums.RoleEnum;
import com.example.msi.models.user.CreateUserDTO;
import com.example.msi.models.user.UpdateUserDTO;
import com.example.msi.models.user.UpdatePasswordUserDTO;
import com.example.msi.models.user.UserDTO;
import com.example.msi.repository.UserRepository;
import com.example.msi.respone.Data;
import com.example.msi.security.CustomUserDetails;
import com.example.msi.service.MailService;
import com.example.msi.service.UserService;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
  private final UserRepository repository;
  private final PasswordEncoder passwordEncoder;
  private final MailService mailService;

  // JWTAuthenticationFilter sẽ sử dụng hàm này
  @Transactional
  public UserDetails loadUserById(Integer id) {
    User user = repository.findById(id).orElseThrow(
        () -> new UsernameNotFoundException("User not found with id : " + id)
    );

    return new CustomUserDetails(user);
  }

  @Override
  public Data register(CreateUserDTO userRegister, StringBuffer siteURL) throws IllegalAccessException, MessagingException {
    Optional<User> optional = repository.findByEmail(userRegister.getEmail());
    if (optional.isPresent()){
      throw new IllegalAccessException("Email đã tồn tại trong hệ thống");
    }
    var user = User.getInstance(userRegister);
    user.setPassword(passwordEncoder.encode(userRegister.getPassword()));
    user.setEnabled(false);
    user.setRole(RoleEnum.STUDENT);// * Mac dinh de Role la Student
    user.setVerificationCode(RandomString.make(64));
    StringBuffer url = new StringBuffer("159.65.4.245/login?verify=");

    Map<String, Object> props = new HashMap<>();
    props.put("email", user.getEmail());
    props.put("url", url.append(user.getVerificationCode()).toString());

    mailService.sendMail(props, user.getEmail(), "sendMail", "Xác thực tài khoản");
    repository.save(user);
    return new Data(UserDTO.getInstance(user));
  }

  @Override
  public Data verify(String verificationCode) throws IllegalAccessException {
    Optional<User> optionalUser = repository.findByVerificationCode(verificationCode);
    if (!optionalUser.isPresent()){
      throw new IllegalAccessException("Verification Code không tồn tại");
    }
    User user = optionalUser.get();
    user.setEnabled(true);
    repository.save(user);
    return new Data(null);
  }

  @Override
  public Data updatePassword(UpdatePasswordUserDTO updatePasswordUserDTO) throws IOException {
    Optional<User> optionalUser = repository.findById(updatePasswordUserDTO.getId());
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      if (passwordEncoder.matches(updatePasswordUserDTO.getPassword(), user.getPassword())) {
        String encryptedPassword = passwordEncoder.encode(updatePasswordUserDTO.getNewPassword());
        user.setPassword(encryptedPassword);
        user.setUpdatePasswordToken(null);
        repository.save(user);
        return new Data(null);
      }
      throw new IOException("Mật khẩu không đúng");
    } else {
      throw new IOException("Không có dữ liệu của người dùng này");
    }
  }

  @Override
  public Data forgotPassword(String mail) throws MessagingException {
    Optional<User> optionalUser = repository.findByEmail(mail);
    if (!optionalUser.isPresent()) return new Data(null);
    String pass = RandomString.make(10);
    User user = optionalUser.get();
    user.setPassword(passwordEncoder.encode(pass));
    repository.save(user);
    Map<String, Object> props = new HashMap<>();
    props.put("email", user.getEmail());
    props.put("pass", pass);

    mailService.sendMail(props, user.getEmail(), "forgotPassword", "Quên mật khẩu");
    return new Data(null);
  }

  @Override
  public Optional<User> userAccessInformation() throws IllegalAccessException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new IllegalAccessException("Chưa có tài khoản đăng nhập");
    }
    String email = authentication.getName();
    Optional<User> user = repository.findByEmail(email);
    return user;
  }

  @Override
  public Data update(UpdateUserDTO updateUser) throws IOException {
    User user = repository.findById(updateUser.getId())
        .orElseThrow(() -> new IOException("id này không tồn tại"));
    user.setDob(updateUser.getDob());
    user.setFullName(updateUser.getFullName());
    user.setPhoneNumber(updateUser.getPhoneNumber());
    repository.save(user);
    return new Data(user);
  }

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user = repository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
    return new CustomUserDetails(user);
  }
}
