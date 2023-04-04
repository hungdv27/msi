package com.example.msi.shared;

import java.util.Arrays;
import java.util.List;

public class Constant {
  public static final int DEFAULT_PAGE_NUMBER = 0;
  public static final int DEFAULT_PAGE_SIZE = 10;
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
  public static final String INCOME_COMPANY_TEMPLATE_STATUS = "Status";
  public static final String F_DUPLICATE = "F_DUPLICATE";
  public static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
  public static final String IMPORT_SUCCESS = "Success";
  public static final String STUDENT_CODE = "code";
  public static final String SQL_CONTAINS_PATTERN = "%%%s%%";
}
