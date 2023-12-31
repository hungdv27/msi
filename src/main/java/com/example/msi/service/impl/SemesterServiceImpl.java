package com.example.msi.service.impl;

import com.example.msi.domains.Semester;
import com.example.msi.service.UserService;
import com.example.msi.shared.exceptions.ExceptionUtils;
import com.example.msi.shared.exceptions.MSIException;
import com.example.msi.models.semester.CreateSemesterDTO;
import com.example.msi.models.semester.UpdateSemesterDTO;
import com.example.msi.repository.SemesterRepository;
import com.example.msi.service.SemesterService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.msi.shared.enums.Role.ADMIN;

@Service
@RequiredArgsConstructor
public class SemesterServiceImpl implements SemesterService {
  private final SemesterRepository repository;
  private final UserService userService;

  @Override
  public List<Semester> getAllSemester() {
    return repository.findAll();
  }

  @Override
  public void addSemester(CreateSemesterDTO semester) throws MSIException {
    if (validateDate(semester.getStartDate(), semester.getEndDate())) {
      var entity = Semester.getInstance(semester);
      repository.save(entity);
    } else {
      throw new MSIException(
          ExceptionUtils.END_DATE_IS_NOT_BEFORE_OR_EQUAL_START_DATE,
          ExceptionUtils.messages.get(ExceptionUtils.END_DATE_IS_NOT_BEFORE_OR_EQUAL_START_DATE));
    }
  }

  @Override
  public void updateSemester(@NonNull UpdateSemesterDTO payload) throws MSIException {
    if (validateDate(payload.getStartDate(), payload.getEndDate())) {
      var id = payload.getId();
      repository.findById(id).ifPresent(entity -> {
        entity.update(payload);
        repository.save(entity);
      });
    } else {
      throw new MSIException(
          ExceptionUtils.END_DATE_IS_NOT_BEFORE_OR_EQUAL_START_DATE,
          ExceptionUtils.messages.get(ExceptionUtils.END_DATE_IS_NOT_BEFORE_OR_EQUAL_START_DATE));
    }
  }

  @Override
  public void deleteSemester(int id) throws MSIException {
    if (id != 0) {
      throw new MSIException("Internship Application Errors.", "Học kỳ tồn tại liên kết đơn đăng ký thực tập của sinh viên.");
    }
    repository.deleteById(id);
  }

  @Override
  public void changeStatus(int id) throws MSIException {
    var optional = repository.findById(id);
    if (optional.isEmpty()) {
      throw new MSIException(
          ExceptionUtils.E_COMMON_NOT_EXISTS_ID,
          String.format(ExceptionUtils.messages.get(ExceptionUtils.E_COMMON_NOT_EXISTS_ID), id));
    }
    var semester = optional.get();
    if (semester.isStatus()) {
      semester.setStatus(false);
    } else {
      if (repository.countAllByStatus(true) > 0) {
        throw new MSIException(
            ExceptionUtils.TRUE_STATUS_IS_EXIST,
            ExceptionUtils.messages.get(ExceptionUtils.TRUE_STATUS_IS_EXIST));
      }
      semester.setStatus(true);
    }
    repository.save(semester);
  }

  @Override
  public Optional<Semester> findSemesterActive() {
    return repository.findTopByStatusIs(true);
  }

  @Override
  @Transactional
  public void acceptInternshipRegistration(int semesterId, boolean acceptStatus, @NonNull String username) {
    if (userService.getRole(username) != ADMIN)
      throw new RuntimeException("Chức năng yêu cầu quyền admin.");
    repository.findAllByAcceptInternshipRegistration(true)
        .forEach(entity -> entity.setAcceptInternshipRegistration(false));
    var semester = repository.findById(semesterId).orElseThrow();
    semester.setAcceptInternshipRegistration(acceptStatus);
  }

  @Override
  public Optional<Semester> findById(int id) {
    return repository.findById(id);
  }

  private boolean validateDate(String startDate, String endDate) {
    var start = LocalDate.parse(startDate);
    var end = LocalDate.parse(endDate);
    return !end.isBefore(start) && !end.isEqual(start);
  }
}
