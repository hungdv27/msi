package com.example.msi.service.impl;

import com.example.msi.domains.*;
import com.example.msi.models.companyresult.CreateCompanyResultDTO;
import com.example.msi.models.companyresult.IncomeCompanyResultCreateDTO;
import com.example.msi.models.companyresult_file.CreateCompanyResultFileDTO;
import com.example.msi.repository.CompanyResultRepository;
import com.example.msi.service.CompanyResultFileService;
import com.example.msi.service.CompanyResultService;
import com.example.msi.service.FileService;
import com.example.msi.shared.Constant;
import com.example.msi.shared.ValidateError;
import com.example.msi.shared.enums.NotificationType;
import com.example.msi.shared.exceptions.ExceptionUtils;
import com.example.msi.shared.exceptions.MSIException;
import com.example.msi.shared.utils.ExcelUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyResultServiceImpl implements CompanyResultService {
  private final CompanyResultRepository repository;
  private final FileService fileService;
  private final CompanyResultFileService companyResultFileService;

  @Override
  @Transactional
  public CompanyResult create(@NonNull CreateCompanyResultDTO dto) throws MSIException, IOException {
    var optional = repository.findTopByStudentCode(dto.getStudentCode());
    optional.ifPresent(cr -> delete(cr.getId()));
    var entity = repository.save(CompanyResult.getInstance(dto));
    attachFiles(entity.getId(), dto.getFiles());
    return entity;
  }

  @Override
  @Transactional
  public void delete(int id) {
    var fileIds = companyResultFileService.findByCompanyResultId(id)
        .stream()
        .map(CompanyResultFile::getFileId)
        .collect(Collectors.toList());
    companyResultFileService.deleteByCompanyResultId(id);
    fileService.deleteByIds(fileIds);
    repository.deleteById(id);
  }

  @Override
  public Optional<CompanyResult> findByStudentCode(String code) {
    return repository.findTopByStudentCode(code);
  }

  @Override
  public List<CompanyResult> findAll() {
    return repository.findAll();
  }

  @Override
  public byte[] templateDownload(HttpServletRequest request) throws IOException {
    var columns = Constant.INCOME_COMPANY_RESULT_IMPORT_HEADER;
    var workbook = new XSSFWorkbook();
    XSSFSheet realSheet = workbook.createSheet("Danh sách Import");
    ExcelUtils.setHeader(columns, workbook, realSheet);
    var bos = new ByteArrayOutputStream();
    workbook.write(bos);
    return bos.toByteArray();
  }

  @Override
  public String importFile(MultipartFile file, HttpServletRequest request) throws IOException, MSIException {
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
    var totalImport = new AtomicInteger(10000);
    var workbook = ExcelUtils.checkFileExcel(file);
    var sheet = workbook.getSheetAt(0);
    var rowHeader = sheet.getRow(0);
    var headerSamples = Constant.INCOME_COMPANY_RESULT_IMPORT_HEADER;
    List<String> headerData = new ArrayList<>();
    for (Cell cell : rowHeader) {
      headerData.add(cell.getStringCellValue());
    }
    boolean checkFile = headerSamples.equals(headerData);
    if (BooleanUtils.isFalse(checkFile)) {
      throw new MSIException(
          ExceptionUtils.E_FILE_IS_NOT_FORMAT_CORRECT,
          ExceptionUtils.messages.get(ExceptionUtils.E_FILE_IS_NOT_FORMAT_CORRECT));
    }
    int totalImportFile = sheet.getLastRowNum();
    if (totalImportFile > totalImport.get()) {
      throw new MSIException(
          ExceptionUtils.E_FILE_DATA_EXCEED_NUMBER_PERMITTED,
          ExceptionUtils.buildMessage(
              ExceptionUtils.E_FILE_DATA_EXCEED_NUMBER_PERMITTED, totalImport.get()));
    }
    List<IncomeCompanyResultCreateDTO> allDataImport = new ArrayList<>();
    var message = this.getDataFromFileImport(sheet, allDataImport);

    workbook.close();
    var duplicates =
        allDataImport.parallelStream()
            .filter(n -> StringUtils.isNotBlank(n.getCheckDuplicate()))
            .collect(Collectors.groupingBy(IncomeCompanyResultCreateDTO::getCheckDuplicate));
    duplicates.entrySet().parallelStream()
        .forEach(
            item -> {
              if (item.getValue().size() > 1) {
                item.getValue()
                    .forEach(n -> n.getStrError()
                        .add(ValidateError.builder()
                            .code(Constant.F_DUPLICATE)
                            .errorMessage("Dữ liệu trùng với dữ liệu trong file")
                            .build()));
              }
            });
    var dataInt = allDataImport.parallelStream()
        .map(CompanyResult::new)
        .collect(Collectors.toList());
    repository.saveAll(dataInt);

    if (message.trim().isEmpty()) {
      return Constant.IMPORT_SUCCESS;
    } else {
      return message.substring(0, message.length() - 2);
    }
  }

  private String getDataFromFileImport(Sheet sheet, List<IncomeCompanyResultCreateDTO> allData) {
    StringBuilder error = new StringBuilder();
    int rowTotal = sheet.getLastRowNum();
    IncomeCompanyResultCreateDTO item;
    var dataFormatter = new DataFormatter();
    for (var i = 1; i <= rowTotal; i++) {
      var row = sheet.getRow(i);
      if (ExcelUtils.isRowEmpty(row)) {
        continue;
      }
      item = new IncomeCompanyResultCreateDTO();
      item.setNumberSort(i);
      this.setDataFromFile(item, dataFormatter, row);
      if (!repository.existsByStudentCode(item.getStudentCode())) {
        if (isValidFloat(item.getCompanyGrade())) {
          allData.add(item);
        } else {
          error.append(String.format("Dòng <%d>: %s |", item.getNumberSort() + 1, "Điểm không phải dạng số"));
        }
      } else {
        error.append(String.format("Dòng <%d>: %s |", item.getNumberSort() + 1, "Mã sinh viên đã tồn tại trong hệ thống"));
      }
    }
    return error.toString();
  }

  private void setDataFromFile(
      IncomeCompanyResultCreateDTO item, DataFormatter dataFormatter, Row row) {
    item.setStudentCode(
        row.getCell(0) != null ? dataFormatter.formatCellValue(row.getCell(0)).trim() : null);
    item.setCompanyGrade(
        row.getCell(1) != null ? dataFormatter.formatCellValue(row.getCell(1)).trim() : null);
    item.setCompanyReview(
        row.getCell(2) != null ? dataFormatter.formatCellValue(row.getCell(2)).trim() : null);
  }

  private void attachFiles(int companyResultFileId, List<MultipartFile> multipartFiles) throws IOException {
    if (multipartFiles == null) return;
    var files = fileService.uploadFiles(multipartFiles);
    for (FileE file : files) {
      var iaf = CreateCompanyResultFileDTO.getInstance(companyResultFileId, file.getId());
      companyResultFileService.add(iaf);
    }
  }

  private boolean isValidFloat(String input) {
    try {
      Float.parseFloat(input);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
