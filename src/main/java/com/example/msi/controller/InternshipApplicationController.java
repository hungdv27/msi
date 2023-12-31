package com.example.msi.controller;

import com.example.msi.models.internshipappication.*;
import com.example.msi.service.InternshipApplicationService;
import com.example.msi.service.SemesterService;
import com.example.msi.service.StudentService;
import com.example.msi.shared.exceptions.MSIException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/internship-application")
@RequiredArgsConstructor
public class InternshipApplicationController {
  private final InternshipApplicationService service;
  private final StudentService studentService;
  private final SemesterService semesterService;

  @PutMapping("/accept-registration")
  public ResponseEntity<Object> acceptRegistration(
      @RequestParam int semesterId,
      @RequestParam boolean acceptStatus,
      Principal principal) {
    semesterService.acceptInternshipRegistration(semesterId, acceptStatus, principal.getName());
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Object> findById(@PathVariable @NonNull int id) {
    var responseData = InternshipApplicationDetailDTO.getInstance(service.findById(id));
    return new ResponseEntity<>(responseData, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<Object> findByUsername(Principal principal) throws MSIException {
    try {
      var responseData = studentService.getAllInternshipApplication(principal.getName()).stream()
          .map(InternshipApplicationDTO::getInstance)
          .collect(Collectors.toList());
      return new ResponseEntity<>(responseData, HttpStatus.OK);
    } catch (Exception e) {
      throw new MSIException(e.getMessage(), "Không tìm thấy tên tài khoản/ Tài khoản chưa liên kết mã sinh viên");
    }
  }

  @GetMapping("/search")
  public ResponseEntity<Page<InternshipApplicationDTO>> search(
      SearchInternshipApplicationDTO searchDTO
  ) {
    var responseData = service.search(searchDTO)
        .map(InternshipApplicationDTO::getInstance);
    return new ResponseEntity<>(responseData, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<InternshipApplicationDetailDTO> create(
      @RequestPart(value = "dto") CreateInternshipApplicationDTO dto,
      @RequestPart(value = "files", required = false) List<MultipartFile> files,
      Principal principal) throws MSIException, IOException {
    dto.setUsername(principal.getName());
    dto.setFiles(files);
    var responseData = InternshipApplicationDetailDTO.getInstance(service.create(dto));
    return new ResponseEntity<>(responseData, HttpStatus.OK);
  }

  @PutMapping
  public ResponseEntity<InternshipApplicationDetailDTO> update(
      @RequestPart(value = "dto") UpdateInternshipApplicationDTO dto,
      @RequestPart(value = "files", required = false) List<MultipartFile> files) {
    dto.setFileNews(files);
    var responseData = (service.update(dto))
        .map(InternshipApplicationDetailDTO::getInstance)
        .orElseThrow(NoSuchElementException::new);
    return new ResponseEntity<>(responseData, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> delete(@PathVariable int id) {
    service.delete(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping("/verify")
  public ResponseEntity<Object> verify(@RequestBody @NonNull VerifyApplicationDTO dto) throws Exception {
    service.verify(dto);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping("/regis-internship/{id}")
  public ResponseEntity<InternshipApplicationDetailDTO> regis(@PathVariable int id) {
    var responData = service.regis(id)
        .map(InternshipApplicationDetailDTO::getInstance)
        .orElseThrow(NoSuchElementException::new);
    return new ResponseEntity<>(responData, HttpStatus.OK);
  }

  @PutMapping("/unregister-internship/{id}")
  public ResponseEntity<InternshipApplicationDetailDTO> cancelRegis(@PathVariable int id) {
    var responData = service.cancelRegis(id)
        .map(InternshipApplicationDetailDTO::getInstance)
        .orElseThrow(NoSuchElementException::new);
    return new ResponseEntity<>(responData, HttpStatus.OK);
  }
}
