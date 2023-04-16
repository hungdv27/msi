package com.example.msi.shared.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ExceptionUtils {
  // Semester
  public static final String END_DATE_IS_NOT_BEFORE_OR_EQUAL_START_DATE = "END_DATE_IS_NOT_BEFORE_OR_EQUAL_START_DATE";
  public static final String TRUE_STATUS_IS_EXIST = "TRUE_STATUS_IS_EXIST";
  public static final String E_COMMON_NOT_EXISTS_ID = "E_COMMON_NOT_EXISTS_ID";

  // Server
  public static final String E_INTERNAL_SERVER = "E_INTERNAL_SERVER";
  // InternshipProcess
  public static final String E_NOT_ADMIN = "E_NOT_ADMIN";
  // ExportExcel
  public static final String E_EXPORT_COMPANY = "E_EXPORT_COMPANY";
  public static final String E_EXPORT_EXCEL = "E_EXPORT_EXCEL";
  public static final String E_FILE_IS_EMPTY = "E_FILE_IS_EMPTY";
  public static final String E_FILE_TOO_LARGE_THAN_DEFAULT_IMPORT_3MB = "E_FILE_TOO_LARGE_THAN_DEFAULT_IMPORT_3MB";
  public static final String E_FILE_IS_NOT_FORMAT_CORRECT = "E_FILE_IS_NOT_FORMAT_CORRECT";
  public static final String E_FILE_DATA_EXCEED_NUMBER_PERMITTED = "E_FILE_DATA_EXCEED_NUMBER_PERMITTED";
  public static final String E_FILE_IS_NOT_EXCEL = "E_FILE_IS_NOT_EXCEL";

  public static final Map<String, String> messages;
  static {
    messages = new HashMap<>();
    messages.put(ExceptionUtils.END_DATE_IS_NOT_BEFORE_OR_EQUAL_START_DATE, "Ngày kết thúc không được nhỏ hơn hoặc bằng ngày bắt đầu");
    messages.put(ExceptionUtils.E_INTERNAL_SERVER, "Server không phản hồi");
    messages.put(ExceptionUtils.E_EXPORT_COMPANY, "Danh sách công ty rỗng");
    messages.put(ExceptionUtils.E_EXPORT_EXCEL, "Lỗi kết xuất excel");
    messages.put(ExceptionUtils.E_FILE_IS_EMPTY, "Vui lòng chọn file");
    messages.put(ExceptionUtils.E_FILE_TOO_LARGE_THAN_DEFAULT_IMPORT_3MB, "File lớn hơn 3MB");
    messages.put(ExceptionUtils.E_FILE_IS_NOT_FORMAT_CORRECT, "File không đúng định dạng template");
    messages.put(ExceptionUtils.E_FILE_DATA_EXCEED_NUMBER_PERMITTED, "File đẩy quá số lượng");
    messages.put(ExceptionUtils.E_FILE_IS_NOT_EXCEL, "File không đúng định dạng excel");
    messages.put(ExceptionUtils.TRUE_STATUS_IS_EXIST, "Status true đã tồn tại");
    messages.put(ExceptionUtils.E_COMMON_NOT_EXISTS_ID, "ID không tồn tại");
    messages.put(ExceptionUtils.E_NOT_ADMIN, "Không phải là admin");
  }

  public static String buildMessage(String messKey, Object... arg) {
    return String.format(ExceptionUtils.messages.get(messKey), arg);
  }
}
