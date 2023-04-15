package com.example.msi.shared.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Slf4j
public class Utils {
  public static String appendLikeExpression(String value) {
    return String.format("%%%s%%", value);
  }

  private static long checkNumber(long number){
    if (number == 0){
      return 1;
    }
    else if (number % 7 == 0){
      return number/7;
    } else {
      return number/7 + 1;
    }
  }

  public static long checkCurrentWeek(LocalDate startDate, LocalDate createDate){
    var daysBetween = ChronoUnit.DAYS.between(startDate, createDate) + 1;
    return checkNumber(daysBetween);
  }
}
