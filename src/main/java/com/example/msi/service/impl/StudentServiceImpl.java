package com.example.msi.service.impl;

import com.example.msi.domains.InternshipApplication;
import com.example.msi.domains.InternshipProcess;
import com.example.msi.domains.Student;
import com.example.msi.models.student.SearchStudentDTO;
import com.example.msi.models.student.UpdateStudentDTO;
import com.example.msi.repository.StudentRepository;
import com.example.msi.service.InternshipApplicationService;
import com.example.msi.service.InternshipProcessService;
import com.example.msi.service.StudentService;
import com.example.msi.service.UserService;
import com.example.msi.shared.enums.InternshipApplicationStatus;
import com.example.msi.shared.exceptions.MSIException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
  private final StudentRepository repository;
  private final UserService userService;
  private final InternshipApplicationService internshipApplicationService;
  private final InternshipProcessService internshipProcessService;

  @Override
  public void updateStudent(@NonNull UpdateStudentDTO payload, String userName) {
    var userId = userService.findByEmail(userName).orElseThrow().getId();
    repository.findTopByUserId(userId).ifPresentOrElse(
        entity -> {
          entity.update(payload);
          repository.save(entity);
        },
        () -> {
          var entity = Student.getInstance(payload, userId);
          repository.save(entity);
        }
    );
  }

  @Override
  public Optional<Student> findByUsername(String userName) {
    var userId = userService.findByEmail(userName).orElseThrow().getId();
    return repository.findTopByUserId(userId);
  }

  @Override
  public Page<Student> search(@NonNull SearchStudentDTO filter) throws MSIException {
    var spec = filter.getSpecification();
    var pageable = filter.getPageable();
    return repository.findAll(spec, pageable);
  }

  @Override
  public Optional<Student> findByCode(@NonNull String code) {
    return repository.findTopByCode(code);
  }

  @Override
  public InternshipProcess getInternshipProcess(@NonNull String username) {
    var studentCode = getStudentCode(username);
    var applicationId = internshipApplicationService
        .findByStudentCodeAndStatus(studentCode, InternshipApplicationStatus.ACCEPTED)
        .orElseThrow(NoSuchElementException::new).getId();
    return internshipProcessService.findByApplicationId(applicationId).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public List<InternshipApplication> getAllInternshipApplication(@NonNull String username) {
    var studentCode = getStudentCode(username);
    return internshipApplicationService.findByStudentCCode(studentCode);
  }

  @Override
  public String getStudentCode(@NonNull String username) {
    var user = userService.findByEmail(username).orElseThrow(NoSuchElementException::new);
    return repository.findTopByUserId(user.getId()).orElseThrow(NoSuchElementException::new).getCode();
  }
}
