package com.example.msi.service.impl;

import com.example.msi.domains.Post;
import com.example.msi.models.post.CreatePostDTO;
import com.example.msi.models.post.UpdatePostDTO;
import com.example.msi.repository.PostRepository;
import com.example.msi.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
  private final PostRepository repository;

  @Override
  public Page<Post> findAll(Pageable pageable) {
    return repository.findAll(pageable);
  }

  @Override
  public Post add(@NonNull CreatePostDTO dto) {
    var entity = Post.getInstance(dto);
    return repository.save(entity);
  }

  @Override
  public Optional<Post> update(@NonNull UpdatePostDTO dto) {
    return repository.findById(dto.getId()).map(post -> {
      post.update(dto);
      return repository.save(post);
    });
  }

  @Override
  public void delete(int id) {
    repository.deleteById(id);
  }
}
