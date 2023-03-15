package com.example.msi.utils;

import com.example.msi.config.annotation.ExportExcel;
import com.example.msi.exceptions.ExceptionUtils;
import com.example.msi.exceptions.MSIException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ExcelUtils {
  public static XSSFWorkbook executeExport(List<Object> list) throws Exception {
    if (CollectionUtils.isEmpty(list)) {
      throw new MSIException("...");
    }

    List<String> headers = new ArrayList<>();
    List<String> exportFields = new ArrayList<>();
    Field[] allFields = list.get(0).getClass().getDeclaredFields();
    for (Field f : allFields) {
      ExportExcel colHeader = f.getAnnotation(ExportExcel.class);
      if (colHeader != null) {
        headers.add(colHeader.title());
        exportFields.add(f.getName());
      }
    }
    XSSFWorkbook workbook = new XSSFWorkbook();
    try {
      XSSFSheet sheet = workbook.createSheet("Export");
      // Add header template
      Row rowHeader = sheet.createRow(0);
      for (int i = 0; i < headers.size(); i++) {
        Cell cell = rowHeader.createCell(i);
        cell.setCellValue(headers.get(i));
      }
      int rowCount = 1;
      String field = null;
      // Add data
      for (Object dto : list) {
        Row rowData = sheet.createRow(rowCount);
        for (int i = 0; i < exportFields.size(); i++) {
          field = exportFields.get(i);
          Cell cell = rowData.createCell(i);
          cell.setCellValue(String.valueOf(callGetter(dto, field)));
        }
        rowCount++;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return workbook;
  }

  private static Object callGetter(Object obj, String fieldName) throws Exception {
    PropertyDescriptor pd;
    try {
      pd = new PropertyDescriptor(fieldName, obj.getClass());
      log.info("Field [{}] - Value[{}]", fieldName, pd.getReadMethod().invoke(obj));
      if (pd.getReadMethod().invoke(obj) == null
          || pd.getReadMethod().invoke(obj) == " "
          || pd.getReadMethod().invoke(obj).equals("null")) {
        return "";
      }
      return pd.getReadMethod().invoke(obj);
    } catch (IntrospectionException
             | IllegalAccessException
             | IllegalArgumentException
             | InvocationTargetException e) {
      e.printStackTrace();
      throw new MSIException("..");
    }
  }

  public static void setHeader(List<String> columns, Workbook wb, Sheet sheet) {
    var rowHeader = sheet.createRow(0);
    for (var i = 0; i < columns.size(); i++) {
      var cell = rowHeader.createCell(i);
      cell.setCellValue(columns.get(i));
      var style = wb.createCellStyle();
      style.setWrapText(true);
      style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
      style.setAlignment(HorizontalAlignment.CENTER);
      style.setVerticalAlignment(VerticalAlignment.CENTER);
      style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      var font = wb.createFont();
      font.setFontName(HSSFFont.FONT_ARIAL);
      font.setFontHeightInPoints((short) 10);
      font.setBold(true);
      style.setFont(font);
      cell.setCellStyle(style);
      sheet.setColumnWidth(i, 20 * 256);
    }
  }

  public static Workbook checkFileExcel(MultipartFile file) throws IOException, MSIException {
    Workbook workbook;
    String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
    assert fileExtension != null;
    if (fileExtension.endsWith("xlsx")) {
      workbook = new XSSFWorkbook(file.getInputStream());
    } else if (fileExtension.endsWith("xls")) {
      workbook = new HSSFWorkbook(file.getInputStream());
    } else {
      throw new MSIException(
          ExceptionUtils.E_FILE_IS_NOT_EXCEL,
          ExceptionUtils.messages.get(ExceptionUtils.E_FILE_IS_NOT_EXCEL));
    }
    return workbook;
  }

  public static boolean isRowEmpty(Row row) {
    boolean isEmpty = true;
    DataFormatter dataFormatter = new DataFormatter();
    if (row != null) {
      for (Cell cell : row) {
        if (dataFormatter.formatCellValue(cell).trim().length() > 0) {
          isEmpty = false;
          break;
        }
      }
    }
    return isEmpty;
  }
}
