package com.example.msi.shared.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utils {
  public static String appendLikeExpression(String value) {
    return String.format("%%%s%%", value);
  }

  public static long checkCurrentWeek(long number){
    if (number == 0){
      return 1;
    }
    else if (number % 7 == 0){
      return number/7;
    } else {
      return number/7 + 1;
    }
  }
}
