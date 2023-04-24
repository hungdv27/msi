package com.example.msi.service.impl;

import com.example.msi.domains.Result;
import com.example.msi.models.result.CreateResultDTO;
import com.example.msi.repository.ResultRepository;
import com.example.msi.service.*;
import com.example.msi.shared.enums.NotificationType;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ResultServiceImpl implements ResultService {
  private final ResultRepository repository;
  private final InternshipProcessService internshipProcessService;
  private final InternshipApplicationService internshipApplicationService;
  private final StudentService studentService;
  private final UserService userService;
  private final NotificationService notificationService;

  @Override
  public Result create(@NonNull CreateResultDTO dto) {
    if (repository.existsByProcessId(dto.getProcessId()))
      throw new RuntimeException("Quá trình thực tập của sinh viên đã tồn tại bản đánh giá.");
    var process = internshipProcessService.findById(dto.getProcessId());
    if (process.isEmpty())
      throw new RuntimeException("Không tìm thấy Process ID");

    var application = internshipApplicationService.findById(process.get().getApplicationId());
    var student = studentService.findByCode(application.getStudentCode());
    var user = userService.findById(student.get().getUserId()).orElseThrow();
    notificationService.sendNotificationAndConvertToQueue(user, "Thông báo",
        "Có kết quả của báo cáo", dto.getProcessId(), NotificationType.RESULT);
    return repository.save(Result.getInstance(dto));
  }

  @Override
  public Optional<Result> findByProcessId(int processId) {
    return repository.findTopByProcessId(processId);
  }
}
