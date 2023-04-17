package com.example.msi.models.post;

import com.example.msi.domains.Post;
import com.example.msi.domains.PostFile;
import com.example.msi.models.file.FileDTO;
import com.example.msi.shared.enums.Role;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.msi.shared.utils.ServiceUtils.getFileService;
import static com.example.msi.shared.utils.ServiceUtils.getPostFileService;

@Getter
public class PostDTO {
  private final int id;
  private final String title;
  private final Role applyTo;
  private final String content;
  private final List<FileDTO> files;
  private final LocalDateTime createdDate;
  private final LocalDateTime updateDate;

  private PostDTO(@NonNull Post target) {
    this.id = target.getId();
    this.title = target.getTitle();
    this.applyTo = target.getApplyTo();
    this.content = target.getContent();
    this.createdDate = target.getCreatedDate();
    this.updateDate = target.getUpdateDate();
    var fileIds = getPostFileService().findByPostId(target.getId())
        .stream()
        .map(PostFile::getFileId)
        .collect(Collectors.toList());
    this.files = getFileService().findByIds(fileIds).stream().map(FileDTO::getInstance).collect(Collectors.toList());
  }

  public static PostDTO getInstance(@NonNull Post entity) {
    return new PostDTO(entity);
  }
}
