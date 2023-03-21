package com.example.msi.service.impl;

import com.example.msi.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {
  private final JavaMailSender mailSender;
  private final SpringTemplateEngine templateEngine;

  @Override
  public void sendMail(Map<String, Object> props, String mail, String template, String subject) throws MessagingException {
    Context context = new Context();
    context.setVariables(props);
    String html = templateEngine.process(template, context);

    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

    helper.setTo(mail);
    helper.setSubject(subject);
    helper.setText(html, true);

    mailSender.send(message);
  }
}
