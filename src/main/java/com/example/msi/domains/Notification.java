package com.example.msi.domains;

import com.example.msi.shared.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
  @ManyToMany
  private List<User> recipients;

  @Column(name = "created_date", updatable = false)
  @CreatedDate
  private LocalDateTime createdDate;
}
