package com.example.msi.shared.converters;

import com.example.msi.shared.enums.Role;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Optional;
import java.util.Set;

import static com.example.msi.shared.converters.Converter.extractFromBitwiseAndEnum;

@Converter
public class RoleConverter implements AttributeConverter<Set<Role>, Integer> {

  @Override
  public Integer convertToDatabaseColumn(Set<Role> attribute) {
    return Optional.ofNullable(attribute)
        .map(com.example.msi.shared.converters.Converter::bitwiseAndEnum)
        .orElseThrow();
  }

  @Override
  public Set<Role> convertToEntityAttribute(Integer dbData) {
    return Optional.ofNullable(dbData)
        .map(value -> extractFromBitwiseAndEnum(value, Role.class))
        .orElseThrow();
  }
}
