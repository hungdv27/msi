package com.example.msi.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ExceptionUtils {
  // Semester
  public static final String END_DATE_IS_NOT_BEFORE_OR_EQUAL_START_DATE = "END_DATE_IS_NOT_BEFORE_OR_EQUAL_START_DATE";

  // Server
  public static final String E_INTERNAL_SERVER = "E_INTERNAL_SERVER";
  // ExportExcel
  public static final String E_EXPORT_COMPANY = "E_EXPORT_COMPANY";
  public static final String E_EXPORT_EXCEL = "E_EXPORT_EXCEL";

  public static final Map<String, String> messages;
  static {
    messages = new HashMap<>();
    messages.put(ExceptionUtils.END_DATE_IS_NOT_BEFORE_OR_EQUAL_START_DATE, "Ngày kết thúc không được nhỏ hơn hoặc bằng ngày bắt đầu");
    messages.put(ExceptionUtils.E_INTERNAL_SERVER, "Server không phản hồi");
    messages.put(ExceptionUtils.E_INTERNAL_SERVER, "Danh sách công ty rỗng");
    messages.put(ExceptionUtils.E_EXPORT_EXCEL, "Lỗi kết xuất excel");
  }
}
