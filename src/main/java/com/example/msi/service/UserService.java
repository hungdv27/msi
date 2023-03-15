package com.example.msi.service;

import com.example.msi.domains.User;
import com.example.msi.models.user.CreateUserDTO;
import com.example.msi.respone.Data;

import javax.mail.MessagingException;
import java.util.Optional;

public interface UserService {
  Data register(CreateUserDTO userRegister, StringBuffer siteURL) throws IllegalAccessException, MessagingException;

  Data verify(String verificationCode) throws IllegalAccessException;

  Data updatePassword(int userId, String password, String newPassword);

  Data forgotPassword(String mail) throws MessagingException;

  Optional<User> userAccessInformation();

}
