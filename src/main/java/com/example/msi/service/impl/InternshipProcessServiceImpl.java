package com.example.msi.service.impl;

import com.example.msi.domains.*;
import com.example.msi.models.internshipprocess.AssignTeacherDTO;
import com.example.msi.models.internshipprocess.CreateInternshipProcessDTO;
import com.example.msi.models.internshipprocess.SearchInternshipProcessDTO;
import com.example.msi.domains.InternshipApplication;
import com.example.msi.domains.InternshipProcess;
import com.example.msi.models.internshipprocess.*;
import com.example.msi.repository.InternshipApplicationRepository;
import com.example.msi.repository.InternshipProcessRepository;
import com.example.msi.repository.ResultRepository;
import com.example.msi.service.*;
import com.example.msi.shared.enums.NotificationType;
import com.example.msi.shared.enums.Role;
import com.example.msi.shared.exceptions.ExceptionUtils;
import com.example.msi.shared.exceptions.MSIException;
import com.example.msi.shared.utils.ExcelUtils;
import com.example.msi.shared.utils.Utils;
import org.springframework.context.annotation.Lazy;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class InternshipProcessServiceImpl implements InternshipProcessService {
  private final InternshipProcessRepository repository;
  private final UserService userService;
  private final NotificationService notificationService;
  private final TeacherService teacherService;
  private final StudentService studentService;
  private final InternshipApplicationRepository internshipApplicationRepository;
  private final ResultRepository resultRepository;
  private final CompanyResultService companyResultService;

  public InternshipProcessServiceImpl(InternshipProcessRepository repository, UserService userService,
                                      NotificationService notificationService, TeacherService teacherService,
                                      @Lazy StudentService studentService, InternshipApplicationRepository internshipApplicationRepository,
                                      ResultRepository resultRepository,
                                      CompanyResultService companyResultService) {
    this.repository = repository;
    this.userService = userService;
    this.notificationService = notificationService;
    this.teacherService = teacherService;
    this.studentService = studentService;
    this.internshipApplicationRepository = internshipApplicationRepository;
    this.resultRepository = resultRepository;
    this.companyResultService = companyResultService;
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
      notificationService.sendNotificationAndConvertToQueue(teacher, "Thông báo",
          "Bạn vừa được phân công hướng dẫn sinh viên", process.getId(), NotificationType.ASSIGN_FOR_TEACHER);

      var student = userService.findById(internshipProcess.getUserId()).orElse(null);
      notificationService.sendNotificationAndConvertToQueue(student, "Thông báo",
          "Bạn vừa được gán giảng viên hướng dẫn", process.getId(), NotificationType.ASSIGN_FOR_STUDENT);

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
    // map các entity
    var internshipApplicationMap = internshipApplicationRepository.findAll().stream()
        .collect(Collectors.toMap(InternshipApplication::getId, ia -> ia));
    var studentMap = studentService.findAll().stream()
        .collect(Collectors.toMap(Student::getCode, s -> s));
    var userMap = userService.findAll().stream()
        .collect(Collectors.toMap(User::getId, u -> u));
    var teacherMap = teacherService.findAll().stream()
        .collect(Collectors.toMap(Teacher::getId, t -> t));
    var resultIterable = resultRepository.findAll();
    var resultList = StreamSupport.stream(resultIterable.spliterator(), false).collect(Collectors.toList());
    var resultMap = resultList.stream().collect(Collectors.toMap(Result::getProcessId, r -> r));
    var companyResultMap = companyResultService.findAll().stream()
        .collect(Collectors.toMap(CompanyResult::getStudentCode, u -> u));

    // chuyển sang list obj
    List<Object> dataExport = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      dataExport.add(new InternshipProcessExportDTO(i + 1, list.get(i), internshipApplicationMap, studentMap,
          userMap, teacherMap, resultMap, companyResultMap));
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