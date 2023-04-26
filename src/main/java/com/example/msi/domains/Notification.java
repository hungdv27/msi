package com.example.msi.domains;

import com.example.msi.shared.enums.NotificationType;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.TypeToken;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Notification {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;

  private String message;

  private NotificationType type;

  @Column(name = "post_id")
  private Integer postId;

  @Column(columnDefinition = "json", name = "user_ids")
  private String userIds;

  @Column(name = "created_date", updatable = false)
  @CreatedDate
  private LocalDateTime createdDate;

  public Set<Integer> getUserIds() {
    return new Gson().fromJson(userIds, new TypeToken<Set<Integer>>() {}.getType());
  }

  public void setUserIds(Set<Integer> userIds) {
    this.userIds = new Gson().toJson(userIds);
  }

}
