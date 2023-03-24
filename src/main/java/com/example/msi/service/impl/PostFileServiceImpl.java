package com.example.msi.service.impl;

import com.example.msi.domains.PostFile;
import com.example.msi.models.postfile.CreatePostFileDTO;
import com.example.msi.repository.PostFileRepository;
import com.example.msi.service.PostFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostFileServiceImpl implements PostFileService {
  private final PostFileRepository repository;

  @Override
  public List<PostFile> findByPostId(@NonNull Integer postId) {
    return repository.findAllByPostId(postId);
  }

  @Override
  public void add(@NonNull CreatePostFileDTO dto) {
    var entity = PostFile.getInstance(dto);
    repository.save(entity);
  }

}
