package com.example.msi.models.postfile;

import lombok.Getter;

@Getter
public class CreatePostFileDTO {
  private int postId;
  private int fileId;

  private CreatePostFileDTO(int postId, int fileId) {
    this.postId = postId;
    this.fileId = fileId;
  }

  public static CreatePostFileDTO getInstance(int postId, int fileId) {
    return new CreatePostFileDTO(postId, fileId);
  }
}
