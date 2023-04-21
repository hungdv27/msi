package com.example.msi.models.student;

import com.example.msi.domains.Student;
import com.example.msi.domains.Teacher;
import com.example.msi.shared.base.BaseFilter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static com.example.msi.shared.Constant.SQL_CONTAINS_PATTERN;
import static com.example.msi.shared.utils.PredicateUtils.build;
import static com.example.msi.shared.utils.PredicateUtils.toPredicate;
import static org.apache.logging.log4j.util.Strings.trimToNull;

@Getter
@RequiredArgsConstructor
public class SearchStudentDTO implements BaseFilter<Student> {
  private final String code;
  private final String fullName;
  private final String gradeCode;
  private final String phoneNumber;
  private final String email;
  private final Integer semesterId;
  private final Integer page;
  private final Integer size;

  public String code() {
    return StringUtils.upperCase(trimToNull(code));
  }

  public String fullName() {
    return StringUtils.upperCase(trimToNull(fullName));
  }
  public String gradeCode() {
    return StringUtils.upperCase(trimToNull(gradeCode));
  }

  public String phoneNumber() {
    return StringUtils.upperCase(trimToNull(phoneNumber));
  }
  public String email() {
    return StringUtils.upperCase(trimToNull(email));
  }

  @Override
  public int page() {
    return Math.max(page == null ? 0 : page, 0);
  }

  @Override
  public int size() {
    return (size == null ? 0 : size) < 1 ? 15 : size;
  }

  @Override
  public Specification<Student> getSpecification() {
    return (root, cq, cb) -> toPredicate(
        List.of(
            Optional.ofNullable(fullName).map(
                value -> build(
                    (expression, o) -> cb.like(cb.lower(expression), o),
                    attr -> root.join("user").get(attr).as(String.class),
                    "fullName",
                    () -> SQL_CONTAINS_PATTERN.formatted(value.toLowerCase())
                )
            ),
            Optional.ofNullable(phoneNumber).map(
                value -> build(cb::equal,
                    attr -> root.join("user").get(attr).as(String.class),
                    "phoneNumber",
                    () -> value)
            ),
            Optional.ofNullable(email).map(
                value -> build(cb::equal,
                    attr -> root.join("user").get(attr).as(String.class),
                    "email",
                    () -> value)
            ),
            Optional.ofNullable(code).map(
                value -> build(cb::equal,
                    attr -> root.get(attr).as(String.class),
                    "code",
                    () -> value)
            ),
            Optional.ofNullable(gradeCode).map(
                value -> build(cb::equal,
                    attr -> root.get(attr).as(String.class),
                    "gradeCode",
                    () -> value)
            ),
            Optional.ofNullable(semesterId).map(
                value -> build(cb::equal,
                    attr -> root.get(attr).as(Integer.class),
                    "semesterId",
                    () -> value)
            )
        ),
        predicates -> cq.where(predicates).getRestriction()
    );
  }
}
