package com.example.msi.service;

import com.example.msi.domains.PostFile;
import com.example.msi.models.postfile.CreatePostFileDTO;
import org.springframework.lang.NonNull;

import java.util.List;

public interface PostFileService {
  List<PostFile> findByPostId(@NonNull Integer postId);

  void add(@NonNull CreatePostFileDTO dto);

  void deleteByPostId(int postId);
  void deleteByFileIds(@NonNull List<Integer> fileIds);
}
