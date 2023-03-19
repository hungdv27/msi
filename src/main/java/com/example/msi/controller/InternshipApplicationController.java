package com.example.msi.controller;

import com.example.msi.models.internshipappication.CreateInternshipApplicationDTO;
import com.example.msi.models.internshipappication.InternshipApplicationDTO;
import com.example.msi.models.internshipappication.SearchInternshipApplicationDTO;
import com.example.msi.models.internshipappication.UpdateInternshipApplicationDTO;
import com.example.msi.service.InternshipApplicationService;
import com.example.msi.shared.enums.InternshipApplicationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/internshipapplication")
@RequiredArgsConstructor
public class InternshipApplicationController {
  private final InternshipApplicationService service;

  @GetMapping("/{id}")
  public ResponseEntity<InternshipApplicationDTO> findById(@PathVariable @NonNull int id) {
    var responseData = InternshipApplicationDTO.getInstance(service.findById(id));
    return new ResponseEntity<>(responseData, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<InternshipApplicationDTO>> search(
      @RequestParam(required = false) String studentCode,
      @RequestParam(required = false) InternshipApplicationStatus status,
      @RequestParam(required = false) Integer fileId,
      @RequestParam(required = false) Integer companyId,
      @RequestParam(required = false) Integer semesterId,
      @RequestParam(required = false,defaultValue = "0") int limit,
      @RequestParam(required = false, defaultValue = "0") int offset
      ) {
    var searchDTO = new SearchInternshipApplicationDTO(
        studentCode,
        status,
        fileId,
        companyId,
        semesterId,
        limit,
        offset
    );
    var responseData = service.search(searchDTO)
        .stream()
        .map(InternshipApplicationDTO::getInstance)
        .collect(Collectors.toList());
    return new ResponseEntity<>(responseData, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<InternshipApplicationDTO> create(
      @RequestBody @NonNull CreateInternshipApplicationDTO dto) {
    var responseData = InternshipApplicationDTO.getInstance(service.create(dto));
    return new ResponseEntity<>(responseData, HttpStatus.OK);

  }

  @PutMapping
  public ResponseEntity<InternshipApplicationDTO> update(
      @RequestBody @NonNull UpdateInternshipApplicationDTO dto) {
    var responseData = (service.update(dto))
        .map(InternshipApplicationDTO::getInstance)
        .orElseThrow(NoSuchElementException::new);
    return new ResponseEntity<>(responseData, HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> delete(@PathVariable int id) {
    service.delete(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
