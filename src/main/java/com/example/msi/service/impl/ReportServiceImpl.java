package com.example.msi.service.impl;

import com.example.msi.domains.*;
import com.example.msi.models.report.CreateReportDTO;
import com.example.msi.models.report.ReportDescriptionDTO;
import com.example.msi.models.reportfile.CreateReportFileDTO;
import com.example.msi.repository.ReportRepository;
import com.example.msi.service.*;
import com.example.msi.shared.enums.InternshipApplicationStatus;
import com.example.msi.shared.enums.NotificationType;
import com.example.msi.shared.exceptions.ExceptionUtils;
import com.example.msi.shared.exceptions.MSIException;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
  private final ReportRepository repository;
  private final StudentService studentService;
  private final InternshipApplicationService internshipApplicationService;
  private final InternshipProcessService internshipProcessService;
  private final FileService fileService;
  private final ReportFileService reportFileService;
  private final UserService userService;
  private final SimpMessagingTemplate messagingTemplate;
  private final NotificationService notificationService;
  private final TeacherService teacherService;

  @Override
  @Transactional
  public Report add(@NonNull CreateReportDTO dto, List<MultipartFile> multipartFiles, @NonNull String username) throws Exception {
    var studentCode = studentService.findByUsername(username).orElseThrow(NoSuchElementException::new).getCode();
    var applicationId = internshipApplicationService.findByStudentCodeAndStatus(studentCode, InternshipApplicationStatus.ACCEPTED)
        .orElseThrow().getId();
    var process = internshipProcessService.findByApplicationId(applicationId).orElseThrow(NoSuchElementException::new);
    // check xem tuần đó đã nộp báo cáo chưa
    var optional = repository.findTopByProcessIdAndWeekNumber(process.getId(), dto.getWeekNumber());
    optional.ifPresent(report -> delete(report.getId()));

    var report = repository.save(Report.getInstance(dto, process.getId()));
    attachFiles(report.getId(), multipartFiles);

    var teacher = teacherService.findById(process.getTeacherId()).orElseThrow(NoSuchElementException::new);
    var user = userService.findById(teacher.getUserId()).orElseThrow(NoSuchElementException::new);
    Set<Integer> userIds = new HashSet<>();
    userIds.add(user.getId());
    Notification notification = new Notification();
    notification.setTitle("Thông Báo");
    notification.setMessage("Có sinh viên vừa nộp báo cáo");
    notification.setUserIds(userIds);
    notification.setType(NotificationType.REPORT);
    notification.setPostId(process.getId());
    notificationService.sendNotification(notification);

    String queueName = "/queue/notification/" + user.getId();
    messagingTemplate.convertAndSend(queueName, notification.getMessage());

    return report;
  }

  @Override
  public Report findById(int id) {
    return repository.findById(id).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public List<Report> findAllByProcessId(int processId) {
    return repository.findAllByProcessId(processId);
  }

  @Override
  public void addDescription(@NonNull ReportDescriptionDTO payload) throws MSIException {
    if (payload.getDescription().length() > 255) {
      throw new MSIException(
          ExceptionUtils.E_DESCRIPTION_TOO_LONG,
          ExceptionUtils.messages.get(ExceptionUtils.E_DESCRIPTION_TOO_LONG));
    }
    repository.findById(payload.getId()).ifPresent(entity -> {
      entity.addDescription(payload);
      repository.save(entity);
    });
  }

  @Override
  @Transactional
  public void delete(int id) {
    var fileIds = reportFileService.findByReportId(id)
        .stream()
        .map(ReportFile::getFileId)
        .collect(Collectors.toList());
    reportFileService.deleteByReportId(id);
    fileService.deleteByIds(fileIds);
    repository.deleteById(id);
  }

  private void attachFiles(int reportId, List<MultipartFile> multipartFiles) throws IOException {
    if (multipartFiles == null) return;
    var files = fileService.uploadFiles(multipartFiles);
    for (FileE file : files) {
      var pf = CreateReportFileDTO.getInstance(reportId, file.getId());
      reportFileService.add(pf);
    }
  }
}
