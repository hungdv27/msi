package com.example.msi.service.impl;

import com.example.msi.domains.*;
import com.example.msi.models.internshipappication.CreateInternshipApplicationDTO;
import com.example.msi.models.internshipappication.SearchInternshipApplicationDTO;
import com.example.msi.models.internshipappication.UpdateInternshipApplicationDTO;
import com.example.msi.models.internshipappication.VerifyApplicationDTO;
import com.example.msi.models.internshipapplication_file.CreateInternshipApplicationFileDTO;
import com.example.msi.models.internshipprocess.CreateInternshipProcessDTO;
import com.example.msi.repository.InternshipApplicationRepository;
import com.example.msi.service.*;
import com.example.msi.shared.enums.InternshipApplicationStatus;
import com.example.msi.shared.enums.NotificationType;
import com.example.msi.shared.enums.Role;
import com.example.msi.shared.exceptions.MSIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.msi.shared.enums.InternshipApplicationStatus.NEW;
import static com.example.msi.shared.enums.InternshipApplicationStatus.WAITING;

@Service
public class InternshipApplicationServiceImpl implements InternshipApplicationService {
  private final InternshipApplicationRepository repository;
  private final FileService fileService;
  private final InternshipApplicationFileService internshipApplicationFileService;
  private final InternshipProcessService internshipProcessService;
  private final SemesterService semesterService;
  private final UserService userService;
  private final NotificationService notificationService;
  private final SimpMessagingTemplate messagingTemplate;
  private final StudentService studentService;

  @Autowired
  public InternshipApplicationServiceImpl(@Lazy StudentService circB, InternshipApplicationRepository repository, FileService fileService, InternshipApplicationFileService internshipApplicationFileService, InternshipProcessService internshipProcessService, SemesterService semesterService, UserService userService, NotificationService notificationService, SimpMessagingTemplate messagingTemplate) {
    this.studentService = circB;
    this.repository = repository;
    this.fileService = fileService;
    this.internshipApplicationFileService = internshipApplicationFileService;
    this.internshipProcessService = internshipProcessService;
    this.semesterService = semesterService;
    this.userService = userService;
    this.notificationService = notificationService;
    this.messagingTemplate = messagingTemplate;
  }

  @Override
  public Page<InternshipApplication> search(@NonNull SearchInternshipApplicationDTO filter) {
    var spec = filter.getSpecification();
    var pageable = filter.getPageable();
    return repository.findAll(spec, pageable);
  }

  @Override
  public InternshipApplication findById(int id) {
    return repository.findById(id).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public List<InternshipApplication> findByStudentCCode(@NonNull String studentCode) {
    return repository.findAllByStudentCode(studentCode);
  }

  @Override
  @Transactional
  public InternshipApplication create(@NonNull CreateInternshipApplicationDTO dto) throws MSIException, IOException {
    var semesterActive = semesterService.findSemesterActive().orElseThrow();
    if (!semesterActive.isAcceptInternshipRegistration())
      throw new RuntimeException("Thời gian đăng ký thực tập chưa bắt đầu");
    var entity = repository.save(InternshipApplication.getInstance(dto));
    attachFiles(entity.getId(), dto.getFiles());
    return entity;
  }

  @Override
  @Transactional
  public Optional<InternshipApplication> update(@NonNull UpdateInternshipApplicationDTO dto) {
    var semesterActive = semesterService.findSemesterActive().orElseThrow();
    if (!semesterActive.isAcceptInternshipRegistration())
      throw new RuntimeException("Thời gian đăng ký thực tập chưa bắt đầu");
    return repository.findById(dto.getId()).map(entity -> {
      entity.update(dto);
      if (dto.getExistedFiles() != null) {
        List<Integer> removeFileIds = new ArrayList<>();
        var fileIds = internshipApplicationFileService.findByInternshipApplicationId(dto.getId())
            .stream().map(InternshipApplicationFile::getFileId).collect(Collectors.toList());
        var fileCurrents = fileService.findByIds(fileIds);
        for (FileE file : fileCurrents) {
          if (!dto.getExistedFiles().contains(file.getFileKey())) {
            removeFileIds.add(file.getId());
          }
        }
        unattachedFiles(removeFileIds);
      }
      try {
        attachFiles(entity.getId(), dto.getFileNews());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      return repository.save(entity);
    });
  }

  @Override
  @Transactional
  public void delete(int id) {
    var fileIds = internshipApplicationFileService.findByInternshipApplicationId(id)
        .stream()
        .map(InternshipApplicationFile::getFileId)
        .collect(Collectors.toList());
    internshipApplicationFileService.deleteByInternshipApplicationId(id);
    fileService.deleteByIds(fileIds);
    repository.deleteById(id);
  }

  @Override
  @Transactional
  public void verify(@NonNull VerifyApplicationDTO dto) {
    var entity = repository.findById(dto.getId()).orElseThrow();
    var accepted = entity.verify(dto);
    if (accepted) {
      var process = new CreateInternshipProcessDTO(entity.getId());
      internshipProcessService.create(process);
    }

    var student = studentService.findByCode(entity.getStudentCode()).orElseThrow();
    var user = userService.findById(student.getUserId()).orElseThrow();

    notificationService.sendNotificationAndConvertToQueue(user, "Thông báo",
        dto.isAccepted() ? "Đơn đăng ký được xét duyệt" : "Đơn đăng ký bị từ chối", entity.getId(), NotificationType.APPLICATION_APPROVE);
  }

  @Override
  @Transactional
  public Optional<InternshipApplication> regis(int id) {
    var semesterActive = semesterService.findSemesterActive().orElseThrow();
    if (!semesterActive.isAcceptInternshipRegistration())
      throw new RuntimeException("Hiện không trong thời gian đăng ký thực tập");
    return repository.findById(id).map(ia -> {
      if (repository.existsByStudentCodeAndStatus(ia.getStudentCode(), WAITING)) {
        throw new RuntimeException("Đã tồn tại yêu cầu duyệt đơn thực tập");
      }
      if (ia.getStatus() == NEW)
        ia.setStatus(WAITING);

      var users = userService.findAllByRole(Role.ADMIN);
      var userIds = users.stream().map(User::getId).collect(Collectors.toSet());
      Notification notification = new Notification();
      notification.setTitle("Thông Báo");
      notification.setMessage("Đơn đăng ký thực tập mới");
      notification.setUserIds(userIds);
      notification.setType(NotificationType.APPLICATION);
      notification.setPostId(id);
      notificationService.sendNotification(notification);

      users.stream()
          .map(User::getId)
          .map(idi -> "/queue/notification/" + idi)
          .forEach(queueName -> messagingTemplate.convertAndSend(queueName, notification.getMessage()));

      return repository.save(ia);
    });
  }

  @Override
  @Transactional
  public Optional<InternshipApplication> cancelRegis(int id) {
    return repository.findById(id).map(ia -> {
      if (ia.getStatus() == WAITING)
        ia.setStatus(NEW);
      return repository.save(ia);
    });
  }

  @Override
  public Optional<InternshipApplication> findByStudentCodeAndStatus(String studentCode, InternshipApplicationStatus status) {
    return repository.findTopByStudentCodeAndStatus(studentCode, status);
  }

  @Override
  public boolean existsBySemesterId(int semesterId) {
    return repository.existsBySemesterId(semesterId);
  }

  @Override
  public List<InternshipApplication> findAll() {
    return repository.findAll();
  }

  private void attachFiles(int internshipApplicationFileId, List<MultipartFile> multipartFiles) throws IOException {
    if (multipartFiles == null) return;
    var files = fileService.uploadFiles(multipartFiles);
    for (FileE file : files) {
      var iaf = CreateInternshipApplicationFileDTO.getInstance(internshipApplicationFileId, file.getId());
      internshipApplicationFileService.add(iaf);
    }
  }

  private void unattachedFiles(@NonNull List<Integer> fileIds) {
    internshipApplicationFileService.deleteByFileIds(fileIds);
    fileService.deleteByIds(fileIds);
  }
}
