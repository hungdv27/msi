package com.example.msi.service;

import com.example.msi.domains.User;
import com.example.msi.models.user.CreateUserDTO;
import com.example.msi.respone.Data;

import javax.mail.MessagingException;

public interface UserService {
  Data register(CreateUserDTO userRegister, StringBuffer siteURL) throws IllegalAccessException, MessagingException;
}
