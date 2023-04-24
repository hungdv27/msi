package com.example.msi.service.impl;

import com.example.msi.domains.Notification;
import com.example.msi.domains.User;
import com.example.msi.repository.NotificationRepository;
import com.example.msi.service.NotificationService;
import com.example.msi.service.UserService;
import com.example.msi.shared.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
  private final NotificationRepository notificationRepository;
  private final UserService userService;
  private final SimpMessagingTemplate messagingTemplate;

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
  public void sendNotificationAndConvertToQueue(User user, String title, String message, Integer postId, NotificationType type) {
    Optional<User> optionalUser = Optional.ofNullable(user);
    optionalUser.ifPresent(u -> {
      Set<Integer> userIds = new HashSet<>();
      userIds.add(u.getId());

      Notification notification = new Notification();
      notification.setTitle(title);
      notification.setMessage(message);
      notification.setUserIds(userIds);
      notification.setType(type);
      notification.setPostId(postId);
      notificationRepository.save(notification);

      String queueName = "/queue/notification/" + u.getId();
      messagingTemplate.convertAndSend(queueName, notification.getMessage());
    });
  }




  @Override
  public Notification getById(Long id) {
    return notificationRepository.findById(id).orElse(null);
  }


}
