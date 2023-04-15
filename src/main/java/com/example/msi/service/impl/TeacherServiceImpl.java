package com.example.msi.service.impl;

import com.example.msi.domains.Teacher;
import com.example.msi.models.teacher.SearchTeacherDTO;
import com.example.msi.models.teacher.UpdateTeacherDTO;
import com.example.msi.repository.TeacherRepository;
import com.example.msi.service.TeacherService;
import com.example.msi.service.UserService;
import com.example.msi.shared.exceptions.MSIException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
  private final TeacherRepository repository;
  private final UserService userService;
  @Override
  public void updateTeacher(@NonNull UpdateTeacherDTO payload, String userName) throws MSIException {
    var user = userService.findByEmail(userName).orElseThrow();
    var userId = user.getId();
    repository.findTopByUserId(userId).ifPresentOrElse(
        entity -> {
          entity.update(payload);
          repository.save(entity);
        },
        () -> {
          var entity = Teacher.getInstance(payload, user);
          repository.save(entity);
        }
    );
  }

  @Override
  public Optional<Teacher> findByUsername(String userName) throws MSIException {
    var userId = userService.findByEmail(userName).orElseThrow().getId();
    return repository.findTopByUserId(userId);
  }

  @Override
  public Page<Teacher> search(@NonNull SearchTeacherDTO filter) throws MSIException {
    var spec = filter.getSpecification();
    var pageable = filter.getPageable();
    return repository.findAll(spec, pageable);
  }

  @Override
  public Teacher save(@NonNull Teacher teacher) {
    return repository.save(teacher);
  }

  @Override
  public Optional<Teacher> findById(int id) {
    return repository.findById(id);
  }
}
