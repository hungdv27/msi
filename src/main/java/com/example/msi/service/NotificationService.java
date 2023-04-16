package com.example.msi.service;

import com.example.msi.domains.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface NotificationService {

  void sendNotification(Notification notification);

  Page<Notification> getAllNotifications(Pageable pageable, String userName);

  Notification getById(Long id);
}
