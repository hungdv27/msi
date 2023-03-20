package com.example.msi.service.impl;

import com.example.msi.domains.Student;
import com.example.msi.exceptions.MSIException;
import com.example.msi.models.student.UpdateStudentDTO;
import com.example.msi.repository.StudentRepository;
import com.example.msi.service.StudentService;
import com.example.msi.service.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
  private final StudentRepository repository;
  private final UserService userService;

  @Override
  public void updateStudent(@NonNull UpdateStudentDTO payload, String userName) throws MSIException {
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
  public Optional<Student> findByUserId(String userName) throws MSIException {
    var userId = userService.findByEmail(userName).orElseThrow().getId();
    return repository.findTopByUserId(userId);
  }
}
