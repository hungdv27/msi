package com.example.msi.shared.converters;

import org.springframework.lang.NonNull;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class Converter {
  private Converter() {}
  public static <T extends Enum<T>> int bitwiseAndEnum(@NonNull Set<T> enumValues) {
    return enumValues.stream()
        .map(item -> Math.pow(2, item.ordinal()))
        .map(Double::intValue)
        .reduce(Integer::sum)
        .orElse(0);
  }
  public static <T extends Enum<T>> Set<T> extractFromBitwiseAndEnum(
      int value,
      @NonNull Class<T> clazz
  ) {
    return Arrays.stream(clazz.getEnumConstants())
        .filter(e -> {
          var v = Math.pow(2, e.ordinal());
          return (value & (int) v) == (int) v;
        }).collect(Collectors.toSet());
  }
}
