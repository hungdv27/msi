package com.example.msi.service.impl;

import com.example.msi.domains.*;
import com.example.msi.models.post.CreatePostDTO;
import com.example.msi.models.post.UpdatePostDTO;
import com.example.msi.models.postfile.CreatePostFileDTO;
import com.example.msi.repository.PostRepository;
import com.example.msi.service.*;
import com.example.msi.shared.enums.NotificationType;
import com.example.msi.shared.enums.PostApplyTo;
import com.example.msi.shared.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
      } else {
        List<Post> posts;
        PostApplyTo applyTo = (val == Role.STUDENT) ? PostApplyTo.STUDENT : PostApplyTo.TEACHER;
        posts = repository.findAllByApplyTo(applyTo);
        posts.addAll(repository.findAllByApplyTo(PostApplyTo.ALL));
        posts.sort(Comparator.comparing(Post::getCreatedDate).reversed());

        int start = (int) pageable1.getOffset();
        int end = Math.min((start + pageable1.getPageSize()), posts.size());
        return new PageImpl<>(posts.subList(start, end), pageable1, posts.size());
      }
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
    PostApplyTo applyTo = post.getApplyTo();
    Role role = (applyTo != PostApplyTo.ALL) ? ((applyTo == PostApplyTo.TEACHER) ? Role.TEACHER : Role.STUDENT) : null;
    HashSet<User> users = new HashSet<>((role != null) ? userService.findAllByRole(role) : userService.findAll());

    attachFiles(post.getId(), multipartFiles);

    var userIds = users.stream().map(User::getId).collect(Collectors.toSet());
    Notification notification = new Notification();
    notification.setTitle("Thông Báo");
    notification.setMessage("Một tin tức mới vừa được đăng");
    notification.setUserIds(userIds);
    notification.setType(NotificationType.POST);
    notification.setPostId(post.getId());
    notificationService.sendNotification(notification);

    users.stream()
        .map(User::getId)
        .map(id -> "/queue/notification/" + id)
        .forEach(queueName -> messagingTemplate.convertAndSend(queueName, notification.getMessage()));

    return post;
  }

  @Override
  @Transactional
  public Optional<Post> update(@NonNull UpdatePostDTO dto) {
    return repository.findById(dto.getId()).map(post -> {
      post.update(dto);
      // unattached file
      if (dto.getExistedFiles() != null) {
        List<Integer> removeFileIds = new ArrayList<>();
        var fileIds = postFileService.findByPostId(dto.getId())
            .stream().map(PostFile::getFileId).collect(Collectors.toList());
        var fileCurrents = fileService.findByIds(fileIds);
        for (FileE file : fileCurrents) {
          if (!dto.getExistedFiles().contains(file.getFileKey())) {
            removeFileIds.add(file.getId());
          }
        }
        unattachedFiles(removeFileIds);
      }
      try {
        attachFiles(post.getId(), dto.getFileNews());
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

  private void unattachedFiles(@NonNull List<Integer> fileIds) {
    postFileService.deleteByFileIds(fileIds);
    fileService.deleteByIds(fileIds);
  }

  @Override
  @Transactional
  public void delete(int id) {
    var fileIds = postFileService.findByPostId(id)
        .stream()
        .map(PostFile::getFileId)
        .collect(Collectors.toList());
    postFileService.deleteByPostId(id);
    fileService.deleteByIds(fileIds);
    repository.deleteById(id);
  }
}
