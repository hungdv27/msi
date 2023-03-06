package com.example.msi.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ExceptionUtils {
  // Semester
  public static final String END_DATE_IS_NOT_BEFORE_OR_EQUAL_START_DATE = "END_DATE_IS_NOT_BEFORE_OR_EQUAL_START_DATE";

  // Server
  public static final String E_INTERNAL_SERVER = "E_INTERNAL_SERVER";

  public static final Map<String, String> messages;
  static {
    messages = new HashMap<>();
    messages.put(ExceptionUtils.END_DATE_IS_NOT_BEFORE_OR_EQUAL_START_DATE, "Ngày kết thúc không được nhỏ hơn hoặc bằng ngày bắt đầu");
    messages.put(ExceptionUtils.E_INTERNAL_SERVER, "Server không phản hồi");
  }
}
