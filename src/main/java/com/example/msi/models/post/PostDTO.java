package com.example.msi.models.post;

import com.example.msi.domains.FileE;
import com.example.msi.domains.Post;
import com.example.msi.domains.PostFile;
import com.example.msi.service.FileService;
import com.example.msi.service.PostFileService;
import com.example.msi.shared.ApplicationContextHolder;
import com.example.msi.shared.enums.Role;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class PostDTO {
  private final int id;
  private final String title;
  private final Role applyTo;
  private final String content;
  private final List<FileE> files;
  private final LocalDateTime createdDate;
  private final LocalDateTime updateDate;

  private PostDTO(@NonNull Post target) {
    this.id = target.getId();
    this.title = target.getTitle();
    this.applyTo = target.getApplyTo();
    this.content = target.getContent();
    this.createdDate = target.getCreatedDate();
    this.updateDate = target.getUpdateDate();
    var fileIds = Singleton.POST_FILE_SERVICE.findByPostId(target.getId())
        .stream()
        .map(PostFile::getFileId)
        .collect(Collectors.toList());
    this.files = Singleton.FILE_SERVICE.findByIds(fileIds);
  }

  public static PostDTO getInstance(@NonNull Post entity) {
    return new PostDTO(entity);
  }

  private static class Singleton {
    private static final PostFileService POST_FILE_SERVICE =
        ApplicationContextHolder.getBean(PostFileService.class);

    private static final FileService FILE_SERVICE =
        ApplicationContextHolder.getBean(FileService.class);
  }
}
