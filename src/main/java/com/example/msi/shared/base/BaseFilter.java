package com.example.msi.shared.base;

import com.example.msi.domains.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface BaseFilter<T> {
  int page();
  int size();
  default Pageable getPageable() {
    return PageRequest.of(page(), size());
  }
  Specification<?> getSpecification();
}