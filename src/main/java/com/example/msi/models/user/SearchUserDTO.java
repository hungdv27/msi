package com.example.msi.models.user;

import com.example.msi.domains.User;
import com.example.msi.shared.base.BaseFilter;
import com.example.msi.shared.enums.Role;
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
public class SearchUserDTO implements BaseFilter<User> {
  private final String email;
  private final Role role;
  private final String fullName;
  private final Boolean enabled;
  private final Integer page;
  private final Integer size;

  public String email() {
    return StringUtils.upperCase(trimToNull(email));
  }

  public String fullName() {
    return StringUtils.upperCase(trimToNull(fullName));
  }

  @Override
  public int size() {
    return (size == null ? 0 : size) < 1 ? 15 : size;
  }

  @Override
  public int page() {
    return Math.max(page == null ? 0 : page, 0);
  }

  @Override
  public Specification<User> getSpecification() {
    return (root, cq, cb) -> toPredicate(
        List.of(
            Optional.ofNullable(email).map(
                value -> build(
                    cb::equal,
                    attr -> root.get(attr).as(String.class),
                    "email",
                    () -> value
                )
            ),
            Optional.ofNullable(role).map(
                value -> build(
                    cb::equal,
                    attr -> root.get(attr).as(Role.class),
                    "role",
                    () -> value
                )
            ),
            Optional.ofNullable(enabled).map(
                value -> build(
                    cb::equal,
                    attr -> root.get(attr).as(Boolean.class),
                    "enabled",
                    () -> value
                )
            )
            ,
            Optional.ofNullable(fullName()).map(
                value -> build(
                    (expression, o) -> cb.like(cb.lower(expression), o),
                    attr -> root.get(attr).as(String.class),
                    "fullName",
                    () -> SQL_CONTAINS_PATTERN.formatted(value.toLowerCase())
                )
            )
        ),
        predicates -> cq.where(predicates).getRestriction()
    );
  }
}
