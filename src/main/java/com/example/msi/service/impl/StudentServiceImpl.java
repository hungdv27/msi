package com.example.msi.service.impl;

import com.example.msi.domains.InternshipApplication;
import com.example.msi.domains.InternshipProcess;
import com.example.msi.domains.Student;
import com.example.msi.models.student.StudentDetailDTO;
import com.example.msi.models.student.UpdateStudentDTO;
import com.example.msi.repository.StudentRepository;
import com.example.msi.service.InternshipApplicationService;
import com.example.msi.service.InternshipProcessService;
import com.example.msi.service.StudentService;
import com.example.msi.service.UserService;
import com.example.msi.shared.Constant;
import com.example.msi.shared.enums.InternshipApplicationStatus;
import com.example.msi.shared.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

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
  public Page<StudentDetailDTO> search(String code, String phoneNumber, String fullName, Pageable pageable) {
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
    var content = page.getContent();
    List<StudentDetailDTO> list = new ArrayList<>();
    for (Student s : content) {
      var studentDetail = new StudentDetailDTO(s);
      list.add(studentDetail);
    }
    return new PageImpl<>(list, page.getPageable(), page.getTotalElements());
  }

  @Override
  public Optional<Student> findByCode(@NonNull String code) {
    return repository.findTopByCode(code);
  }

  @Override
  public InternshipProcess getInternshipProcess(@NonNull String username) {
    var studentCode = repository.findTopByCode(username).orElseThrow(NoSuchElementException::new).getCode();
    var applicationId = internshipApplicationService
        .findByStudentCodeAndStatus(studentCode, InternshipApplicationStatus.ACCEPTED)
        .orElseThrow(NoSuchElementException::new).getId();
    return internshipProcessService.findByApplicationId(applicationId).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public List<InternshipApplication> getAllInternshipApplication(@NonNull String username) {
    var studentCode = repository.findTopByCode(username).orElseThrow(NoSuchElementException::new).getCode();
    return internshipApplicationService.findByStudentCCode(studentCode);
  }
}
