package com.example.msi.service.impl;

import com.example.msi.domains.InternshipApplication;
import com.example.msi.domains.InternshipProcess;
import com.example.msi.models.internshipprocess.AssignTeacherDTO;
import com.example.msi.models.internshipprocess.CreateInternshipProcessDTO;
import com.example.msi.models.internshipprocess.SearchInternshipProcessDTO;
import com.example.msi.repository.InternshipProcessRepository;
import com.example.msi.service.InternshipProcessService;
import com.example.msi.service.UserService;
import com.example.msi.shared.enums.Role;
import com.example.msi.shared.exceptions.ExceptionUtils;
import com.example.msi.shared.exceptions.MSIException;
import com.example.msi.shared.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InternshipProcessServiceImpl implements InternshipProcessService {
  private final InternshipProcessRepository repository;
  private final UserService userService;

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
    });
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
}