package com.example.msi.service.impl;

import com.example.msi.domains.FileE;
import com.example.msi.domains.Notification;
import com.example.msi.domains.Post;
import com.example.msi.domains.User;
import com.example.msi.models.post.CreatePostDTO;
import com.example.msi.models.post.UpdatePostDTO;
import com.example.msi.models.postfile.CreatePostFileDTO;
import com.example.msi.repository.PostRepository;
import com.example.msi.service.*;
import com.example.msi.shared.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
  private final PostRepository repository;
  private final UserService userService;
  private final FileService fileService;
  private final PostFileService postFileService;
  private final SimpMessagingTemplate messagingTemplate;
  private final NotificationService notificationService;

  @Override
  public Page<Post> findAll(Pageable pageable, @NonNull String userName) {
    Sort sort1 = Sort.by("createdDate").descending();
    Pageable pageable1 = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort1);
    return userService.findByEmail(userName).map(User::getRole).map(val -> {
      if (val == Role.ADMIN) {
        return repository.findAll(pageable1);
      } else return repository.findAllByApplyTo(val, pageable1);
    }).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public Post findById(int id) {
    return repository.findById(id).orElseThrow(NoSuchElementException::new);
  }

  @Override
  @Transactional
  public Post add(@NonNull CreatePostDTO dto, List<MultipartFile> multipartFiles) throws Exception {
    var post = repository.save(Post.getInstance(dto));
    attachFiles(post.getId(), multipartFiles);
    List<User> recipients = userService.findAllByRole(Role.STUDENT);
    Notification notification = new Notification();
    notification.setTitle("New post created");
    notification.setMessage("A new post has been created: " + post.getTitle());
    notification.setRecipients(recipients);
    notificationService.sendNotification(notification);

    List<String> studentEmails = userService.findAllEmailByRole(Role.STUDENT);
    for (String username : studentEmails) {
      messagingTemplate.convertAndSendToUser(username, "/topic/notifications", notification);
    }

    return post;
  }

  @Override
  @Transactional
  public Optional<Post> update(@NonNull UpdatePostDTO dto) {
    return repository.findById(dto.getId()).map(post -> {
      post.update(dto);
      if (dto.getFiles() != null) {
        unattachedFiles(post.getId());
      }
      try {

        attachFiles(post.getId(), dto.getFiles());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      return repository.save(post);
    });
  }

  private void attachFiles(int postId, List<MultipartFile> multipartFiles) throws IOException {
    if (multipartFiles == null) return;
    var files = fileService.uploadFiles(multipartFiles);
    for (FileE file : files) {
      var pf = CreatePostFileDTO.getInstance(postId, file.getId());
      postFileService.add(pf);
    }
  }

  private void unattachedFiles(int postId) {
    postFileService.deleteByPostId(postId);
  }

  @Override
  @Transactional
  public void delete(int id) {
    postFileService.deleteByPostId(id);
    repository.deleteById(id);
  }
}
