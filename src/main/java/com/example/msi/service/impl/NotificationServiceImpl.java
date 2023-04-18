package com.example.msi.service.impl;

import com.example.msi.domains.Notification;
import com.example.msi.repository.NotificationRepository;
import com.example.msi.service.NotificationService;
import com.example.msi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
  private final NotificationRepository notificationRepository;
  private final UserService userService;

  @Override
  public void sendNotification(Notification notification) {
    notificationRepository.save(notification);
  }

  @Override
  public Page<Notification> getAllNotifications(Pageable pageable, String userName) {
    var user = userService.findByEmail(userName).orElseThrow();
    var userId = user.getId();
    var allNotification = notificationRepository.findAll();
    List<Notification> notifications =  new ArrayList<>();
    allNotification.stream().forEach(
        value -> {
          if(value.getUserIds().contains(userId)){
            notifications.add(value);
          }
        }
    );
    System.out.println(notifications);
    Sort sort1 = Sort.by("created_date").descending();
    Pageable pageable1 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort1);
    var allNotifications = notificationRepository.findAllNotificationByUser(pageable1, userId);

    return allNotifications;
  }

  @Override
  public Notification getById(Long id) {
    return notificationRepository.findById(id).orElse(null);
  }


}
