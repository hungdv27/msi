package com.example.msi.service.impl;

import com.example.msi.domains.Student;
import com.example.msi.models.student.UpdateStudentDTO;
import com.example.msi.repository.StudentRepository;
import com.example.msi.service.StudentService;
import com.example.msi.service.UserService;
import com.example.msi.shared.Constant;
import com.example.msi.shared.utils.Utils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
  private final StudentRepository repository;
  private final UserService userService;

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
  public Page<Student> search(String code, String phoneNumber, String fullName, Pageable pageable) {
    pageable =
        PageRequest.of(
            pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Constant.ID).ascending());
    Specification<Student> spec =
        (root, cq, cb) -> {
          List<Predicate> predicates = new ArrayList<>();
          if (isNotEmpty(StringUtils.trimToEmpty(code))) {
            predicates.add(
                cb.like(
                    cb.upper(root.get(Constant.STUDENT_CODE)),
                    Utils.appendLikeExpression(StringUtils.trimToEmpty(code).toUpperCase())));
          }
          if (isNotEmpty(StringUtils.trimToEmpty(phoneNumber))) {
            predicates.add(
                cb.like(
                    cb.upper(root.join("user").get("phoneNumber")),
                    Utils.appendLikeExpression(StringUtils.trimToEmpty(phoneNumber).toUpperCase())));
          }
          if (isNotEmpty(StringUtils.trimToEmpty(fullName))) {
            predicates.add(
                cb.like(
                    cb.upper(root.join("user").get("fullName")),
                    Utils.appendLikeExpression(StringUtils.trimToEmpty(fullName).toUpperCase())));
          }
          return cq.where(predicates.toArray(new Predicate[0])).getRestriction();
        };
    Page<Student> page = repository.findAll(spec, pageable);
    return new PageImpl<>(page.getContent(), page.getPageable(), page.getTotalElements());
  }
}
