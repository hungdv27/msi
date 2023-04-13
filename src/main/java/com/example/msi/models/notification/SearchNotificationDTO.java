package com.example.msi.models.notification;

import com.example.msi.domains.Notification;
import com.example.msi.domains.User;
import com.example.msi.shared.base.BaseFilter;
import com.example.msi.shared.enums.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import static com.example.msi.shared.utils.PredicateUtils.toPredicate;

@Getter
@RequiredArgsConstructor
public class SearchNotificationDTO implements BaseFilter<Notification> {
  private final Integer page;
  private final Integer size;
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
        List.of(),
        predicates -> cq.where(predicates).getRestriction()
    );
  }
}
