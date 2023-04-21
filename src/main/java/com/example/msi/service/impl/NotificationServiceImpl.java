package com.example.msi.service.impl;

import com.example.msi.domains.Notification;
import com.example.msi.repository.NotificationRepository;
import com.example.msi.service.NotificationService;
import com.example.msi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
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
  @Transactional
  public Page<Notification> getAllNotifications(Pageable pageable, String userName) {
    var user = userService.findByEmail(userName).orElseThrow();
    var userId = user.getId();
    user.setLastReadTime(LocalDateTime.now());
    Sort sort = Sort.by("createdDate").descending(); // Sắp xếp theo trường createdDate, giảm dần
    Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort); // Kết hợp Pageable với Sort
    Page<Notification> allNotification = notificationRepository.findAllByUserIdsContaining(userId, pageableWithSort); // Sử dụng câu truy vấn với Sort
    return allNotification;
  }




  @Override
  public Notification getById(Long id) {
    return notificationRepository.findById(id).orElse(null);
  }


}
