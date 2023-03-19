package com.example.msi.shared.utils;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;

import org.springframework.lang.NonNull;

public class PredicateUtils {
  private PredicateUtils() {
  }

  public static <X> Predicate build(
      @NonNull BiFunction<Expression<X>, X, Predicate> getPredicate,
      @NonNull Function<String, Expression<X>> getExpression,
      @NonNull String attributeName,
      @NonNull Supplier<X> supplier
  ) {
    var expression = getExpression.apply(attributeName);
    var value = supplier.get();
    return getPredicate.apply(expression, value);
  }

  public static Predicate toPredicate(
      @NonNull List<Optional<Predicate>> optionals,
      @NonNull Function<Predicate[], Predicate> converter
  ) {
    var predicates = optionals.stream()
        .filter(Optional::isPresent)
        .map(Optional::get)
        .toArray(Predicate[]::new);
    return converter.apply(predicates);
  }
}