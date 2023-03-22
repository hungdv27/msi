package com.example.msi.models.post;

import com.example.msi.domains.Post;
import com.example.msi.shared.enums.PostApplyTo;
import com.example.msi.shared.enums.Role;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
public class PostDTO {
  private final int id;
  private final String title;
  private final Set<Role> applyTo;
  private final String content;
  private final LocalDateTime createdDate;
  private final LocalDateTime updateDate;

  private PostDTO(@NonNull Post target){
    this.id = target.getId();
    this.title = target.getTitle();
    this.applyTo = target.getApplyTo();
    this.content = target.getContent();
    this.createdDate = target.getCreatedDate();
    this.updateDate = target.getUpdateDate();
  }

  public static PostDTO getInstance(@NonNull Post entity){
    return new PostDTO(entity);
  }
}
