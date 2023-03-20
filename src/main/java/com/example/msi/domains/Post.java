package com.example.msi.domains;

import com.example.msi.shared.converters.PostRoleApplyToConverter;
import com.example.msi.shared.enums.PostApplyTo;
import com.example.msi.models.post.CreatePostDTO;
import com.example.msi.models.post.UpdatePostDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Table(name = "post")
public class Post {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "title", nullable = false)
  private String title;

  @Convert(converter = PostRoleApplyToConverter.class)
  @Column(name = "audience", nullable = false)
  private Set<PostApplyTo> applyTo;

  @Column(name = "content", nullable = false)
  private String content;

  @Column(name = "created_date", nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdDate;

  @Column(name = "update_date")
  @LastModifiedDate
  private LocalDateTime updateDate;

  private Post(@NonNull CreatePostDTO target) {
    title = target.getTitle();
    applyTo = target.getApplyTo();
    content = target.getContent();
  }

  public void update(@NonNull UpdatePostDTO dto) {
    title = dto.getTitle();
    applyTo = dto.getApplyTo();
    content = dto.getContent();
  }

  public static Post getInstance(@NonNull CreatePostDTO dto) {
    return new Post(dto);
  }
}
