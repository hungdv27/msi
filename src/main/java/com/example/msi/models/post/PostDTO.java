package com.example.msi.models.post;

import com.example.msi.domains.Post;
import com.example.msi.enums.PostApplyTo;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
public class PostDTO {
  private final String title;
  private final Set<PostApplyTo> applyTo;
  private final String content;
  private final LocalDateTime createdDate;

  private PostDTO(@NonNull Post target){
    this.title = target.getTitle();
    this.applyTo = target.getApplyTo();
    this.content = target.getContent();
    this.createdDate = target.getCreatedDate();
  }

  public static PostDTO getInstance(@NonNull Post entity){
    return new PostDTO(entity);
  }
}
