package com.example.msi.repository;

import com.example.msi.domains.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
  @Query(value = "SELECT * FROM notification WHERE JSON_SEARCH(user_ids, 'one', :userId) IS NOT NULL", nativeQuery = true)
  Page<Notification> findAllNotificationByUser(Pageable pageable, @Param("userId") Integer userId);


}
