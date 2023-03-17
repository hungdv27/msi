package com.example.msi.service;

import com.example.msi.domains.Post;
import com.example.msi.models.post.CreatePostDTO;
import com.example.msi.models.post.UpdatePostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface PostService {
  Page<Post> findAll(Pageable pageable);

  Post add(@NonNull CreatePostDTO dto);

  Optional<Post> update(@NonNull UpdatePostDTO dto);

  void delete(int id);
}
