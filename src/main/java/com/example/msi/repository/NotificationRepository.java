package com.example.msi.repository;

import com.example.msi.domains.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
  @Query(value = "SELECT * FROM notification n join notification_recipients nr on nr.notification_id = n.id join user u on nr.recipients_id = u.id Where u.email = :username",
      nativeQuery = true)
  Page<Notification> findAllNotificationByUser(Pageable pageable, String username);
}
