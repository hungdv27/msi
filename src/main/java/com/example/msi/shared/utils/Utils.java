package com.example.msi.shared.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utils {
  public static String appendLikeExpression(String value) {
    return String.format("%%%s%%", value);
  }
}
