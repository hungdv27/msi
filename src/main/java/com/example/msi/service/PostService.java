package com.example.msi.service;

import com.example.msi.domains.Post;
import com.example.msi.models.post.CreatePostDTO;
import com.example.msi.models.post.UpdatePostDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface PostService {
  Page<Post> findAll(Pageable pageable, @NonNull String username);

  Post findById(int id);

  Post add(@NonNull CreatePostDTO dto, MultipartFile multipartFile);

  Optional<Post> update(@NonNull UpdatePostDTO dto);

  void delete(int id);
}
