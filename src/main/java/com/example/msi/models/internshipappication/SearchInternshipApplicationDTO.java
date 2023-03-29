package com.example.msi.models.internshipappication;

import com.example.msi.domains.InternshipApplication;
import com.example.msi.shared.base.BaseFilter;
import com.example.msi.shared.enums.InternshipApplicationStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;


import java.util.List;
import java.util.Optional;

import static com.example.msi.shared.utils.PredicateUtils.build;
import static com.example.msi.shared.utils.PredicateUtils.toPredicate;
import static org.apache.logging.log4j.util.Strings.trimToNull;

@Getter
@RequiredArgsConstructor
public class SearchInternshipApplicationDTO implements BaseFilter<InternshipApplication> {
  private final String studentCode;
  private final InternshipApplicationStatus status;
  private final Integer companyId;
  private final Integer semesterId;
  private final Integer page = 0;
  private final Integer size = 0;

  public String studentCode() {
    return StringUtils.upperCase(trimToNull(studentCode));
  }

  @Override
  public int size() {
    return size < 1 ? 15 : size;
  }

  @Override
  public int page() {
    return Math.max(page, 0);
  }


  @Override
  public Specification<InternshipApplication> getSpecification() {
    return (root, cq, cb) -> toPredicate(
        List.of(
            Optional.ofNullable(studentCode()).map(
                value -> build(
                    cb::equal,
                    attr -> root.get(attr).as(String.class),
                    "studentCode",
                    () -> value
                )
            ),
            Optional.ofNullable(status).map(
                value -> build(
                    cb::equal,
                    attr -> root.get(attr).as(InternshipApplicationStatus.class),
                    "status",
                    () -> value
                )
            ),
            Optional.ofNullable(companyId).map(
                value -> build(
                    cb::equal,
                    attr -> root.get(attr).as(Integer.class),
                    "companyId",
                    () -> value
                )
            ),
            Optional.ofNullable(semesterId).map(
                value -> build(
                    cb::equal,
                    attr -> root.get(attr).as(Integer.class),
                    "semesterId",
                    () -> value
                )
            )
        ),
        predicates -> cq.where(predicates).getRestriction()
    );
  }
}
