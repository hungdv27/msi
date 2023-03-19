package com.example.msi.shared.converters;

import com.example.msi.shared.enums.PostApplyTo;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;
import java.util.Set;

import static com.example.msi.shared.converters.Converter.extractFromBitwiseAndEnum;

@Converter
public class PostRoleApplyToConverter implements AttributeConverter<Set<PostApplyTo>, Integer> {

  @Override
  public Integer convertToDatabaseColumn(Set<PostApplyTo> attribute) {
    return Optional.ofNullable(attribute)
        .map(com.example.msi.shared.converters.Converter::bitwiseAndEnum)
        .orElseThrow();
  }

  @Override
  public Set<PostApplyTo> convertToEntityAttribute(Integer dbData) {
    return Optional.ofNullable(dbData)
        .map(value -> extractFromBitwiseAndEnum(value, PostApplyTo.class))
        .orElseThrow();
  }
}
