package com.example.msi.service.impl;

import com.example.msi.domains.Notification;
import com.example.msi.repository.NotificationRepository;
import com.example.msi.service.NotificationService;
import com.example.msi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    Sort sort = Sort.by("createdDate").descending(); // Sắp xếp giảm dần theo createdDate
    List<Notification> notifications = notificationRepository.findAll(sort); // Lấy danh sách thông báo đã được sắp xếp

    List<Notification> userNotifications = notifications.stream()
        .filter(notification -> notification.getUserIds().contains(userId))
        .collect(Collectors.toList()); // Lọc ra danh sách thông báo của userId

    int pageSize = pageable.getPageSize();
    int currentPage = pageable.getPageNumber();
    int startItem = currentPage * pageSize;
    List<Notification> pageNotifications;
    if (userNotifications.size() < startItem) {
      pageNotifications = Collections.emptyList();
    } else {
      int toIndex = Math.min(startItem + pageSize, userNotifications.size());
      pageNotifications = userNotifications.subList(startItem, toIndex);
    }

    // Tạo đối tượng PageImpl với danh sách thông báo của trang hiện tại và thông tin phân trang
    Page<Notification> page = new PageImpl<>(pageNotifications, pageable, userNotifications.size());

    return page;
  }



  @Override
  public Notification getById(Long id) {
    return notificationRepository.findById(id).orElse(null);
  }


}
