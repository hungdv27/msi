package com.example.msi.models.internshipprocess;

import com.example.msi.domains.InternshipProcess;
import com.example.msi.shared.Constant;
import com.example.msi.shared.base.BaseFilter;
import lombok.Data;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static com.example.msi.shared.utils.PredicateUtils.build;
import static com.example.msi.shared.utils.PredicateUtils.toPredicate;
import static org.apache.logging.log4j.util.Strings.trimToNull;

@Getter
@Data
public class SearchInternshipProcessDTO implements BaseFilter<InternshipProcess> {
  private String studentCode;
  private Integer teacherId;
  private Integer semesterId;
  private String courseCode;
  private Integer page;
  private Integer size;

  public SearchInternshipProcessDTO() {}

  public SearchInternshipProcessDTO(String studentCode, Integer teacherId, Integer semesterId, String courseCode) {
    this.studentCode = studentCode;
    this.teacherId = teacherId;
    this.semesterId = semesterId;
    this.courseCode = courseCode;
    this.page = page();
    this.size = Constant.MAX_PAGE_SIZE;
  }

  public String studentCode() {
    return StringUtils.upperCase(trimToNull(studentCode));
  }

  public String courseCode() {
    return StringUtils.upperCase(trimToNull(courseCode));
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
  public Specification<InternshipProcess> getSpecification() {
    return (root, cq, cb) -> toPredicate(
        List.of(
            Optional.ofNullable(studentCode()).map(
                value -> build(
                    cb::equal,
                    attr -> root.join("internshipApplication").get(attr).as(String.class),
                    "studentCode",
                    () -> value)
            ),
            Optional.ofNullable(semesterId).map(value -> build(
                cb::equal, attr -> root.join("internshipApplication").get(attr).as(Integer.class),
                "semesterId",
                () -> value)
            ),
            Optional.ofNullable(courseCode()).map(
                value -> build(
                    cb::equal, attr -> root.join("internshipApplication").get(attr).as(String.class),
                    "courseCode",
                    () -> value)
            ),
            Optional.ofNullable(teacherId).map(
                value -> build(cb::equal,
                    attr -> root.get(attr).as(Integer.class),
                    "teacherId",
                    () -> value)
            )
        ),
        predicates -> cq.where(predicates).getRestriction());
  }
}
