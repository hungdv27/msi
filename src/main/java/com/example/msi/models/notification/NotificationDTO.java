package com.example.msi.models.notification;

import com.example.msi.domains.Notification;
import com.example.msi.shared.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
  private long id;
  private String message;
  private String title;
  private NotificationType type;
  private Integer postId;
  private LocalDateTime createdDate;

  private NotificationDTO(@NonNull Notification target) {
    id = target.getId();
    message = target.getMessage();
    title = target.getTitle();
    type = target.getType();
    postId = target.getPostId();
    createdDate = target.getCreatedDate();
  }

  public static NotificationDTO getInstance(@NonNull Notification entity) {
    return new NotificationDTO(entity);
  }
}
