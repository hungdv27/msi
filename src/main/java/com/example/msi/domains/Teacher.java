package com.example.msi.domains;

import com.example.msi.models.teacher.UpdateTeacherDTO;
import lombok.*;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teacher")
public class Teacher {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "user_id", insertable = false, updatable = false)
  private Integer userId;

  @Column(name = "created_date", nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  @NotAudited
  @Setter(AccessLevel.NONE)
  private User user;

  @Column(name = "status", nullable = false)
  private boolean status;

  @Column(name = "updated_date")
  @LastModifiedDate
  private LocalDateTime updatedDate;

  private Teacher(@NonNull UpdateTeacherDTO target, User userId) {
    this.status = target.isStatus();
    user = userId;
  }

  public void update(@NonNull UpdateTeacherDTO target) {
    this.status = target.isStatus();
  }

  public static Teacher getInstance(@NonNull UpdateTeacherDTO payload, User userId) {
    return new Teacher(payload, userId);
  }

}
