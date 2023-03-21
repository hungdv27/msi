package com.example.msi.service.impl;

import com.example.msi.domains.Post;
import com.example.msi.domains.User;
import com.example.msi.models.post.CreatePostDTO;
import com.example.msi.models.post.UpdatePostDTO;
import com.example.msi.repository.PostRepository;
import com.example.msi.service.PostService;
import com.example.msi.service.UserService;
import com.example.msi.shared.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
  private final PostRepository repository;
  private final UserService userService;

  @Override
  public Page<Post> findAll(Pageable pageable, @NonNull String userName) {
    Sort sort = Sort.by("createdDate").descending();
    Pageable pageable1 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    var role = userService.findByEmail(userName).map(User::getRole).get();
    return repository.findAll(pageable);
//    return repository.findAll(pageable1).stream()
//        .filter(post -> role == Role.ADMIN || post.getApplyTo().contains(role))
//        .findAny()
//        .stream()
//        .collect(Collectors.toList());
  }

  @Override
  public Post findById(int id) {
    return repository.findById(id).orElseThrow(NoSuchElementException::new);
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
