package com.example.msi.service.impl;

import com.example.msi.domains.InternshipApplication;
import com.example.msi.domains.InternshipProcess;
import com.example.msi.models.internshipprocess.AssignTeacherDTO;
import com.example.msi.models.internshipprocess.SearchInternshipProcessDTO;
import com.example.msi.repository.InternshipProcessRepository;
import com.example.msi.service.InternshipApplicationService;
import com.example.msi.service.InternshipProcessService;
import com.example.msi.service.StudentService;
import com.example.msi.service.UserService;
import com.example.msi.shared.enums.InternshipApplicationStatus;
import com.example.msi.shared.enums.Role;
import com.example.msi.shared.exceptions.MSIException;
import com.example.msi.shared.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InternshipProcessServiceImpl implements InternshipProcessService {
  private final InternshipProcessRepository repository;
  private final StudentService studentService;
  private final InternshipApplicationService internshipApplicationService;
  private final UserService userService;

  @Override
  public void assignTeacher(@NonNull AssignTeacherDTO dto, @NonNull String username) throws MSIException {
    var role = userService.findByEmail(username).orElseThrow().getRole();
    if (role.equals(Role.ADMIN)){
      assignTeacherByAdmin(dto);
    } else {
      var studentCode = studentService.findByUsername(username).orElseThrow().getCode();
      var entity = setTeacherId(dto, studentCode);
      repository.save(entity);
    }
  }

  @Override
  public Optional<InternshipProcess> findByApplicationId(int applicationId) {
    return repository.findTopByApplicationId(applicationId);
  }

  @Override
  public InternshipProcess findById(int id) {
    return repository.findById(id).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public InternshipProcess findByMe(String username) throws MSIException {
    var studentCode = studentService.findByUsername(username).orElseThrow(NoSuchElementException::new).getCode();
    var applicationId = internshipApplicationService
        .findByStudentCodeAndStatus(studentCode, InternshipApplicationStatus.ACCEPTED)
        .orElseThrow(NoSuchElementException::new).getId();
    var idProcess = repository.findTopByApplicationId(applicationId).orElseThrow(NoSuchElementException::new).getId();
    return repository.findById(idProcess).orElseThrow(NoSuchElementException::new);
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

  private void assignTeacherByAdmin(@NonNull AssignTeacherDTO dto) {
    var processList = dto.getStudentCodeList().stream()
        .map(studentCode -> setTeacherId(dto, studentCode))
        .collect(Collectors.toList());
    repository.saveAll(processList);
  }

  private InternshipProcess setTeacherId(@NonNull AssignTeacherDTO dto, String studentCode) {
    var applicationId = internshipApplicationService.findByStudentCodeAndStatus(studentCode, InternshipApplicationStatus.ACCEPTED)
        .orElseThrow().getId();
    var entity = repository.findTopByApplicationId(applicationId).orElseThrow();
    entity.setTeacherId(dto.getTeacherId());
    return entity;
  }
}