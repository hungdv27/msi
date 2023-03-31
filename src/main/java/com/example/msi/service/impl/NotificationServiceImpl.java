package com.example.msi.service.impl;

import com.example.msi.domains.Notification;
import com.example.msi.repository.NotificationRepository;
import com.example.msi.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
  private final NotificationRepository notificationRepository;

  @Override
  public void sendNotification(Notification notification) {
    notificationRepository.save(notification);
  }

}
