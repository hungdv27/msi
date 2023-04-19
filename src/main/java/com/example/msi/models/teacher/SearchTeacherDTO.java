package com.example.msi.models.teacher;

import com.example.msi.domains.Teacher;
import com.example.msi.shared.base.BaseFilter;
import com.example.msi.shared.enums.Role;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static com.example.msi.shared.Constant.SQL_CONTAINS_PATTERN;
import static com.example.msi.shared.utils.PredicateUtils.build;
import static com.example.msi.shared.utils.PredicateUtils.toPredicate;

@Data
@RequiredArgsConstructor
public class SearchTeacherDTO implements BaseFilter<Teacher> {
  private final String email;
  private final String fullName;
  private final Boolean status;
  private final Integer page;
  private final Integer size;

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
                    attr -> root.get(attr).as(Boolean.class),
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
