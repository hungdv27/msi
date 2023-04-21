package com.example.msi.models.teacher;

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
public class SearchTeacherDTO implements BaseFilter<Teacher> {
  private final String email;
  private final String fullName;
  private final Boolean status;
  private final Integer page;
  private final Integer size;

  public String email() {
    return StringUtils.upperCase(trimToNull(email));
  }

  public String fullName() {
    return StringUtils.upperCase(trimToNull(fullName));
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
  public Specification<Teacher> getSpecification() {
    return (root, cq, cb) -> toPredicate(
        List.of(
            Optional.ofNullable(status).map(
                value -> build(
                    cb::equal,
                    attr -> root.get(attr).as(boolean.class),
                    "status",
                    () -> value
                )
            ),
            Optional.ofNullable(fullName).map(
                value -> build(
                    (expression, o) -> cb.like(cb.lower(expression), o),
                    attr -> root.join("user").get(attr).as(String.class),
                    "fullName",
                    () -> SQL_CONTAINS_PATTERN.formatted(value.toLowerCase())
                )
            ),
            Optional.ofNullable(email).map(
                value -> build(
                    (expression, o) -> cb.like(cb.lower(expression), o),
                    attr -> root.join("user").get(attr).as(String.class),
                    "email",
                    () -> SQL_CONTAINS_PATTERN.formatted(value.toLowerCase())
                )
            )
        ),
        predicates -> cq.where(predicates).getRestriction()
    );
  }
}
