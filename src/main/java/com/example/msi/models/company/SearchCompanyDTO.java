package com.example.msi.models.company;

import com.example.msi.domains.Company;
import com.example.msi.shared.Constant;
import com.example.msi.shared.base.BaseFilter;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static com.example.msi.shared.Constant.SQL_CONTAINS_PATTERN;
import static com.example.msi.shared.utils.PredicateUtils.build;
import static com.example.msi.shared.utils.PredicateUtils.toPredicate;
import static org.apache.logging.log4j.util.Strings.trimToNull;

@Getter
@Data
public class SearchCompanyDTO implements BaseFilter<Company> {
  private String name;
  private Integer page;
  private Integer size;

  public SearchCompanyDTO() {}

  public SearchCompanyDTO(String name) {
    this.name = name;
    this.page = page();
    this.size = Constant.MAX_PAGE_SIZE;
  }

  public String name() {
    return StringUtils.upperCase(trimToNull(name));
  }

  @Override
  public int size() {
    return (((size == null) ? 0 : size) < 1) ? 15 : size;
  }

  @Override
  public int page() {
    return Math.max(page == null ? 0 : page, 0);
  }

  @Override
  public Specification<Company> getSpecification() {
    return (root, cq, cb) -> toPredicate(
        List.of(
            Optional.ofNullable(name).map(
                value -> build(
                    (expression, o) -> cb.like(cb.lower(expression), o),
                    attr -> root.get(attr).as(String.class),
                    "name",
                    () -> SQL_CONTAINS_PATTERN.formatted(value.toLowerCase())
                )
            )
        ),
        predicates -> cq.where(predicates).getRestriction());
  }
}
