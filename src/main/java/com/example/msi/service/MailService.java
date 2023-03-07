package com.example.msi.service;

import javax.mail.MessagingException;
import java.util.Map;

public interface MailService {
  void sendMail(Map<String, Object> props, String mail, String template, String subject) throws MessagingException;
}
