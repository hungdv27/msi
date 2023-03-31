package com.example.msi.service;

import com.example.msi.domains.Notification;
import org.springframework.stereotype.Service;

@Service
public interface NotificationService {

  void sendNotification(Notification notification);

//  void saveNotification(Notification notification);
}
