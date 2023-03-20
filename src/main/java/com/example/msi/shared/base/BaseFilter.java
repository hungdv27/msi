package com.example.msi.shared.base;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface BaseFilter<T> {
  int offset();
  int limit();
  default Pageable getPageable() {
    var page = offset() / limit();
    return PageRequest.of(page, limit());
  }
  Specification<T> getSpecification();
}