package com.example.msi.shared;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Arrays;
import java.util.List;

public class Constant {
  public static final int DEFAULT_PAGE_NUMBER = 0;
  public static final int DEFAULT_PAGE_SIZE = 10;
  public static final int MAX_PAGE_SIZE = 1000;
  public static final String ID = "id";
  public static final List<String> INCOME_COMPANY_IMPORT_HEADER =
      Arrays.asList(
          Constant.INCOME_COMPANY_TEMPLATE_NAME,
          Constant.INCOME_COMPANY_TEMPLATE_PHONE_NUMBER,
          Constant.INCOME_COMPANY_TEMPLATE_EMAIL,
          Constant.INCOME_COMPANY_TEMPLATE_ADDRESS);
  public static final String INCOME_COMPANY_TEMPLATE_NAME = "Tên công ty";
  public static final String INCOME_COMPANY_TEMPLATE_PHONE_NUMBER = "Số điện thoại";
  public static final String INCOME_COMPANY_TEMPLATE_EMAIL = "Email";
  public static final String INCOME_COMPANY_TEMPLATE_ADDRESS = "Địa chỉ";

  public static final List<String> INCOME_COMPANY_RESULT_IMPORT_HEADER =
      Arrays.asList(
          Constant.INCOME_COMPANY_RESULT_TEMPLATE_STUDENT_CODE,
          Constant.INCOME_COMPANY_RESULT_TEMPLATE_GRADE,
          Constant.INCOME_COMPANY_RESULT_TEMPLATE_REVIEW);
  public static final String INCOME_COMPANY_RESULT_TEMPLATE_STUDENT_CODE = "Mã sinh viên";
  public static final String INCOME_COMPANY_RESULT_TEMPLATE_GRADE = "Điểm đánh giá công ty";
  public static final String INCOME_COMPANY_RESULT_TEMPLATE_REVIEW = "Nhận xét công ty";

  public static final List<String> INCOME_TEACHER_ACCOUNT_IMPORT_HEADER =
      Arrays.asList(
          Constant.INCOME_TEACHER_ACCOUNT_TEMPLATE_NAME,
          Constant.INCOME_TEACHER_ACCOUNT_PHONE_NUMBER,
          Constant.INCOME_TEACHER_ACCOUNT_EMAIL,
          Constant.INCOME_TEACHER_ACCOUNT_DATEOFBIRTH);
  public static final String INCOME_TEACHER_ACCOUNT_TEMPLATE_NAME = "Họ Tên";
  public static final String INCOME_TEACHER_ACCOUNT_EMAIL = "Email";
  public static final String INCOME_TEACHER_ACCOUNT_PHONE_NUMBER = "Số Điện Thoại";
  public static final String INCOME_TEACHER_ACCOUNT_DATEOFBIRTH = "Ngày Sinh";

  public static final String F_DUPLICATE = "F_DUPLICATE";
  public static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
  public static final String IMPORT_SUCCESS = "Success";
  public static final String STUDENT_CODE = "code";
  public static final String SQL_CONTAINS_PATTERN = "%%%s%%";

  public static final DateTimeFormatter DATETIME_FORMATTER_2 =
      new DateTimeFormatterBuilder().appendPattern("M/d/")
          .optionalStart()
          .appendPattern("uuuu")
          .optionalEnd()
          .optionalStart()
          .appendValueReduced(ChronoField.YEAR, 2, 2, 1920)
          .optionalEnd()
          .toFormatter();
}
