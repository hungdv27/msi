package com.example.msi.controller;

import com.example.msi.models.companyresult.CompanyResultDTO;
import com.example.msi.models.companyresult.CreateCompanyResultDTO;
import com.example.msi.models.error.ErrorDTO;
import com.example.msi.response.ImportError;
import com.example.msi.response.ImportSuccess;
import com.example.msi.service.CompanyResultService;
import com.example.msi.shared.Constant;
import com.example.msi.shared.exceptions.ExceptionUtils;
import com.example.msi.shared.exceptions.MSIException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/company-result")
@RequiredArgsConstructor
@Slf4j
public class CompanyResultController {
  private final CompanyResultService service;

  @PostMapping
  public ResponseEntity<CompanyResultDTO> create(
      @RequestPart(value = "dto") CreateCompanyResultDTO dto,
      @RequestPart(value = "files", required = false) List<MultipartFile> files) throws MSIException, IOException {
    dto.setFiles(files);
    var responseData = CompanyResultDTO.getInstance(service.create(dto));
    return new ResponseEntity<>(responseData, HttpStatus.OK);
  }

  @GetMapping(value = "/template/download", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> templateDownload(HttpServletRequest request) {
    try {
      var bytes = service.templateDownload(request);
      return new ResponseEntity<>(bytes, HttpStatus.OK);
    } catch (Exception ex) {
      log.error(ex.getMessage());
      return new ResponseEntity<>(
          ExceptionUtils.messages.get(ExceptionUtils.E_INTERNAL_SERVER),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping(value = "/import-file", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> importFile(
      @RequestBody MultipartFile file, HttpServletRequest request) {
    try {
      var message = service.importFile(file, request);
      if (message.equals(Constant.IMPORT_SUCCESS)) {
        return new ResponseEntity<>(new ImportSuccess(message), HttpStatus.OK);
      }
      return new ResponseEntity<>(new ImportError(message), HttpStatus.OK);
    } catch (MSIException ex) {
      log.error(ex.getMessage());
      return new ResponseEntity<>(
          new ErrorDTO(ex.getMessageKey(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    } catch (Exception ex) {
      log.error(ex.getMessage());
      return new ResponseEntity<>(
          ExceptionUtils.messages.get(ExceptionUtils.E_INTERNAL_SERVER),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
