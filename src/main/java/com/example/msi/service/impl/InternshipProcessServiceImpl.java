package com.example.msi.service.impl;

import com.example.msi.domains.*;
import com.example.msi.models.internshipprocess.AssignTeacherDTO;
import com.example.msi.models.internshipprocess.CreateInternshipProcessDTO;
import com.example.msi.models.internshipprocess.SearchInternshipProcessDTO;
import com.example.msi.domains.InternshipApplication;
import com.example.msi.domains.InternshipProcess;
import com.example.msi.domains.Notification;
import com.example.msi.models.internshipprocess.*;
import com.example.msi.repository.InternshipApplicationRepository;
import com.example.msi.repository.InternshipProcessRepository;
import com.example.msi.repository.StudentRepository;
import com.example.msi.service.*;
import com.example.msi.shared.enums.NotificationType;
import com.example.msi.shared.enums.Role;
import com.example.msi.shared.exceptions.ExceptionUtils;
import com.example.msi.shared.exceptions.MSIException;
import com.example.msi.shared.utils.ExcelUtils;
import com.example.msi.shared.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.*;

@Service
public class InternshipProcessServiceImpl implements InternshipProcessService {
  private final InternshipProcessRepository repository;
  private final UserService userService;
  private final SimpMessagingTemplate messagingTemplate;
  private final NotificationService notificationService;
  private final TeacherService teacherService;
  private final StudentService studentService;
  private final InternshipApplicationRepository internshipApplicationRepository;

  public InternshipProcessServiceImpl(InternshipProcessRepository repository, UserService userService, SimpMessagingTemplate messagingTemplate, NotificationService notificationService, TeacherService teacherService, @Lazy StudentService studentService, InternshipApplicationRepository internshipApplicationRepository) {
    this.repository = repository;
    this.userService = userService;
    this.messagingTemplate = messagingTemplate;
    this.notificationService = notificationService;
    this.teacherService = teacherService;
    this.studentService = studentService;
    this.internshipApplicationRepository = internshipApplicationRepository;
  }

  @Override
  @Transactional
  public void assignTeacher(@NonNull AssignTeacherDTO dto, @NonNull String username) throws MSIException {
    var role = userService.findByEmail(username).orElseThrow().getRole();
    if (!role.equals(Role.ADMIN)) {
      throw new MSIException(
          ExceptionUtils.E_NOT_ADMIN,
          ExceptionUtils.messages.get(ExceptionUtils.E_NOT_ADMIN));
    }
    var teacherId = dto.getTeacherId();
    dto.getApplicationId().forEach(applicationId -> {
      var process = repository.findTopByApplicationId(applicationId).orElseThrow();
      process.setTeacherId(teacherId);
      repository.save(process);

      var internshipProcess = internshipApplicationRepository.findById(process.getApplicationId())
          .map(i -> studentService.findByCode(i.getStudentCode()).orElse(null))
          .orElse(null);

      var teacher = findUserByTeacherId(process.getTeacherId());
      sendNotificationAndConvertToQueue(teacher, "Phân Công Hướng Dẫn Sinh Viên",
          "Phân Công Hướng Dẫn Sinh Viên", process.getId());

      var student = userService.findById(internshipProcess.getUserId()).orElse(null);
      sendNotificationAndConvertToQueue(student, "Phân Công Giáo Viên Hướng Dẫn",
          "Phân Công Giáo Viên Hướng Dẫn", process.getId());


    });
  }

  private void sendNotificationAndConvertToQueue(User user, String title, String message, Integer postId) {
    Optional<User> optionalUser = Optional.ofNullable(user);
    optionalUser.ifPresent(u -> {
      Set<Integer> userIds = new HashSet<>();
      userIds.add(u.getId());

      Notification notification = new Notification();
      notification.setTitle(title);
      notification.setMessage(message);
      notification.setUserIds(userIds);
      notification.setType(NotificationType.REPORT);
      notification.setPostId(postId);
      notificationService.sendNotification(notification);

      String queueName = "/queue/notification/" + u.getId();
      messagingTemplate.convertAndSend(queueName, notification.getMessage());
    });
  }

  // Phương thức tìm kiếm user theo teacherId
  private User findUserByTeacherId(Integer teacherId) {
    return teacherService.findById(teacherId)
        .map(t -> userService.findById(t.getUserId()).orElse(null))
        .orElse(null);
  }



  @Override
  public Optional<InternshipProcess> findByApplicationId(int applicationId) {
    return repository.findTopByApplicationId(applicationId);
  }

  @Override
  public Optional<InternshipProcess> findById(int id) {
    return repository.findById(id);
  }

  @Override
  public long currentWeekProcess(InternshipApplication internshipApplication) {
    var checkCreatedDate = LocalDate.now();
    var checkStartDate = internshipApplication.getStartDate();
    return Utils.checkCurrentWeek(checkStartDate, checkCreatedDate);
  }

  @Override
  public Page<InternshipProcess> search(@NonNull SearchInternshipProcessDTO filter) {
    var spec = filter.getSpecification();
    var pageable = filter.getPageable();
    return repository.findAll(spec, pageable);
  }

  @Override
  public InternshipProcess create(@NonNull CreateInternshipProcessDTO dto) {
    var entity = InternshipProcess.getInstance(dto);
    return repository.save(entity);
  }

  @Override
  public Object export(HttpServletRequest request, Object req) throws MSIException {
    SearchInternshipProcessDTO reqDTO = (SearchInternshipProcessDTO) req;

    Page<InternshipProcess> page = this.search(reqDTO);
    List<InternshipProcess> list = page.getContent();
    if (list.isEmpty()) {
      throw new MSIException(
          ExceptionUtils.E_EXPORT_INTERNSHIP_PROCESS,
          ExceptionUtils.messages.get(ExceptionUtils.E_EXPORT_INTERNSHIP_PROCESS));
    }
    // chuyển sang list obj
    List<Object> dataExport = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      dataExport.add(new InternshipProcessExportDTO(i + 1, list.get(i)));
    }
    // tạo workbook
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try (Workbook workbook = ExcelUtils.executeExport(dataExport);
         bos) {
      workbook.write(bos);
    } catch (Exception e) {
      throw new MSIException(
          ExceptionUtils.E_EXPORT_EXCEL,
          ExceptionUtils.messages.get(ExceptionUtils.E_EXPORT_EXCEL));
    }
    return bos.toByteArray();
  }
}