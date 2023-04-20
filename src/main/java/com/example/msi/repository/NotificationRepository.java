package com.example.msi.repository;

import com.example.msi.domains.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
  Page<Notification> findAllByUserIdsContaining(Integer userId, Pageable pageable);
}
