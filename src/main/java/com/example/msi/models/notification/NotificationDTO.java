package com.example.msi.models.notification;

import com.example.msi.domains.Notification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
  private long id;
  private String message;
  private String title;

  private NotificationDTO(@NonNull Notification target) {
    id = target.getId();
    message = target.getMessage();
    title = target.getTitle();
  }

  public static NotificationDTO getInstance(@NonNull Notification entity) {
    return new NotificationDTO(entity);
  }
}
