package com.example.msi.service.impl;

import com.example.msi.domains.Company;
import com.example.msi.exceptions.ExceptionUtils;
import com.example.msi.exceptions.MSIException;
import com.example.msi.models.company.*;
import com.example.msi.repository.CompanyRepository;
import com.example.msi.service.CompanyService;
import com.example.msi.shared.Constant;
import com.example.msi.shared.ValidateError;
import com.example.msi.utils.ExcelUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {
  private final CompanyRepository repository;

  private static final int DEFAULT_PAGE_NUMBER = 0;
  private static final int DEFAULT_PAGE_SIZE = 10;

  public static Pageable getPageable(Integer page, Integer size) {
    page = page != null ? page : DEFAULT_PAGE_NUMBER;
    size = size != null ? size : DEFAULT_PAGE_SIZE;
    return PageRequest.of(page, size);
  }

  @Override
  public Page<Company> getAllCompany(Pageable pageable) {
    return repository.findAll(pageable);
  }

  @Override
  public Company getCompanyById(int id) {
    return repository.findById(id).orElse(null);
  }

  @Override
  public Company addCompany(CreateCompanyDTO payload) {
    var entity = Company.getInstance(payload);
    return repository.save(entity);
  }

  @Override
  public Optional<Company> updateCompany(@NonNull UpdateCompanyDTO payload) {
    var id=payload.getId();
    return repository.findById(id).map(e ->{
      e.update(payload);
      return repository.save(e);
    });
  }

  @Override
  public List<Company> searchCompanyByName(@NonNull String name) {
    return repository.getCompanyByName(name);
  }

  @Override
  public void deleteCompany(int id) {
    repository.deleteById(id);
  }

  @Override
  public Object export(HttpServletRequest request, Object req) throws MSIException {
    CompanyReqDTO reqDTO = (CompanyReqDTO) req;
    reqDTO.setPageable(
        PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Constant.ID).ascending()));

    Page<Company> page = this.getAllCompany(reqDTO.getPageable());
    List<Company> list = page.getContent();
    if (list.isEmpty()) {
      throw new MSIException(
          ExceptionUtils.E_EXPORT_COMPANY,
          ExceptionUtils.messages.get(ExceptionUtils.E_EXPORT_COMPANY));
    }
    // chuyển sang list obj
    List<Object> dataExport = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      dataExport.add(new CompanyExportDTO(i + 1, list.get(i)));
    }
    // tạo workbook
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try (Workbook workbook = ExcelUtils.executeExport(dataExport);
         bos) {
      workbook.write(bos);
    } catch (Exception e) {
      throw new MSIException(
          ExceptionUtils.E_EXPORT_EXCEL,
          ExceptionUtils.messages.get(ExceptionUtils.E_EXPORT_EXCEL));
    }
    return bos.toByteArray();
  }

  @Override
  public byte[] templateDownload(HttpServletRequest request) throws IOException {
    // Số lượng bản ghi được cấu hình chp phép import
    var totalImport = new AtomicInteger(10000);

    var columns = Constant.INCOME_COMPANY_IMPORT_HEADER;
    var workbook = new XSSFWorkbook();
    XSSFSheet realSheet = workbook.createSheet("Danh sách Import");

    // Style phần header
    ExcelUtils.setHeader(columns, workbook, realSheet);
    var bos = new ByteArrayOutputStream();
    workbook.write(bos);
    return bos.toByteArray();
  }

  @Override
  public String importFile(MultipartFile file, HttpServletRequest request) throws IOException, MSIException {

    //Check chưa đính kèm file
    if (file.isEmpty()) {
      throw new MSIException(
          ExceptionUtils.E_FILE_IS_EMPTY,
          ExceptionUtils.messages.get(ExceptionUtils.E_FILE_IS_EMPTY));
    }
    double fileSize = file.getBytes().length;
    if (fileSize / (1024 * 1024) > 3) {
      throw new MSIException(
          ExceptionUtils.E_FILE_TOO_LARGE_THAN_DEFAULT_IMPORT_3MB,
          ExceptionUtils.messages.get(ExceptionUtils.E_FILE_TOO_LARGE_THAN_DEFAULT_IMPORT_3MB));
    }
    // Số lượng bản ghi được cấu hình chp phép import
    var totalImport = new AtomicInteger(10000);

    //Kiểm tra file đẩy lên có đúng form
    var workbook = ExcelUtils.checkFileExcel(file);

    //Đọc dữ liệu sheet đầu tiên
    var sheet = workbook.getSheetAt(0);
    var rowHeader = sheet.getRow(0);

    // Validate form file excel như file mẫu (hoặc theo file danh sách lỗi)
    var headerSamples = Constant.INCOME_COMPANY_IMPORT_HEADER;
    List<String> headerData = new ArrayList<>();
    for (Cell cell : rowHeader) {
      headerData.add(cell.getStringCellValue());
    }
    boolean checkFile = headerSamples.equals(headerData);

    // check file không đúng định dạng
    if (BooleanUtils.isFalse(checkFile)) {
      throw new MSIException(
          ExceptionUtils.E_FILE_IS_NOT_FORMAT_CORRECT,
          ExceptionUtils.messages.get(ExceptionUtils.E_FILE_IS_NOT_FORMAT_CORRECT));
    }
    // check file đẩy quá số lượng dữ liệu cho phép tron phần cấu hính
    int totalImportFile = sheet.getLastRowNum();
    if (totalImportFile > totalImport.get()) {
      throw new MSIException(
          ExceptionUtils.E_FILE_DATA_EXCEED_NUMBER_PERMITTED,
          ExceptionUtils.buildMessage(
              ExceptionUtils.E_FILE_DATA_EXCEED_NUMBER_PERMITTED, totalImport.get()));
    }
    // Xử lý dữ liệu trong file import
    List<IncomeCompanyCreateDTO> allDataImport = new ArrayList<>();
    var message = this.getDataFromFileImport(sheet, allDataImport);

    workbook.close();
    // validate duplicate
    var duplicates =
        allDataImport.parallelStream()
            .filter(n -> StringUtils.isNotBlank(n.getCheckDuplicate()))
            .collect(Collectors.groupingBy(IncomeCompanyCreateDTO::getCheckDuplicate));
    duplicates.entrySet().parallelStream()
        .forEach(
            item -> {
              if (item.getValue().size() > 1) {
                item.getValue()
                    .forEach(
                        n ->
                            n.getStrError()
                                .add(
                                    ValidateError.builder()
                                        .code(Constant.F_DUPLICATE)
                                        .errorMessage("Dữ liệu trùng với dữ liệu trong file")
                                        .build()));
              }
            });
//    Lưu dữ liệu
    var dataInt =
        allDataImport.parallelStream()
            .map(
                item -> {
                  var entityInt = new Company(item);
                  return entityInt;
                })
            .collect(Collectors.toList());
    repository.saveAll(dataInt);

    if (message.trim().isEmpty()) {
      return Constant.IMPORT_SUCCESS;
    } else {
      return message.substring(0, message.length() - 2);
    }
  }

  // mapping data from file
  private String getDataFromFileImport(Sheet sheet, List<IncomeCompanyCreateDTO> allData) {
    String error = "";
    int rowTotal = sheet.getLastRowNum();
    //  Đọc dữ liệu trong file bỏ dòng header(dòng 1).
    IncomeCompanyCreateDTO item;
    var dataFormatter = new DataFormatter();
    for (var i = 1; i <= rowTotal; i++) {
      // lấy giá trị từng row
      var row = sheet.getRow(i);
      // Bỏ qua dòng nào trống
      if (ExcelUtils.isRowEmpty(row)) {
        continue;
      }
      item = new IncomeCompanyCreateDTO();
      // STT
      item.setNumberSort(i);
      // set data
      this.setDataFromFile(item, dataFormatter, row);
      // validate email của từng row
      if (!repository.existsByName(item.getName())) {
        if (validateEmail(item.getEmail())) {
          if (!repository.existsByEmail(item.getEmail())) {
            allData.add(item);
          } else {
            error += String.format("Row <%d>: %s |", item.getNumberSort(), "Email duplicate");
          }
        } else {
          error += String.format("Row <%d>: %s |", item.getNumberSort(), "Email invalid");
        }
      } else {
        error += String.format("Row <%d>: %s |", item.getNumberSort(), "CompanyName duplicate");
      }
    }
    return error;
  }

  // set data import file
  private void setDataFromFile(
      IncomeCompanyCreateDTO item, DataFormatter dataFormatter, Row row) {
    item.setName(
        row.getCell(0) != null ? dataFormatter.formatCellValue(row.getCell(0)).trim() : null);
    item.setPhoneNumber(
        row.getCell(1) != null ? dataFormatter.formatCellValue(row.getCell(1)).trim() : null);
    item.setEmail(
        row.getCell(2) != null ? dataFormatter.formatCellValue(row.getCell(2)).trim() : null);
    item.setAddress(
        row.getCell(3) != null ? dataFormatter.formatCellValue(row.getCell(3)).trim() : null);
  }

  private boolean validateEmail (String email) {
    return Pattern.compile(Constant.EMAIL_REGEX).matcher(email).matches();
  }
}
