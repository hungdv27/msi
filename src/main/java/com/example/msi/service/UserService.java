package com.example.msi.service;

import com.example.msi.domains.User;
import com.example.msi.models.user.CreateUserDTO;
import com.example.msi.models.user.UpdateUserDTO;
import com.example.msi.models.user.UpdatePasswordUserDTO;
import com.example.msi.response.Data;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Optional;

public interface UserService {
  Data register(CreateUserDTO userRegister, StringBuffer siteURL) throws IllegalAccessException, MessagingException;

  Data verify(String verificationCode) throws IllegalAccessException;

  Data updatePassword(UpdatePasswordUserDTO updatePasswordUser) throws IOException;

  Data forgotPassword(String mail) throws MessagingException;

  Optional<User> userAccessInformation() throws IllegalAccessException;

  Data update(UpdateUserDTO updateUser) throws IOException;

  Optional<User> findByEmail(String mail);

  Optional<User> findById(int id);

}
