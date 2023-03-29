package com.example.msi.controller;

import com.example.msi.models.internshipappication.*;
import com.example.msi.service.InternshipApplicationService;
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

  @GetMapping("/{id}")
  public ResponseEntity<InternshipApplicationDTO> findById(@PathVariable @NonNull int id) {
    var responseData = InternshipApplicationDTO.getInstance(service.findById(id));
    return new ResponseEntity<>(responseData, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<InternshipApplicationDTO>> findByUsername(Principal principal) throws MSIException {
    var responseData = service.findByUsername(principal.getName()).stream()
        .map(InternshipApplicationDTO::getInstance)
        .collect(Collectors.toList());
    return new ResponseEntity<>(responseData, HttpStatus.OK);
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
  public ResponseEntity<InternshipApplicationDTO> create(
      @RequestParam("companyId") int companyId,
      @RequestParam("note") String note,
      @ModelAttribute("files") List<MultipartFile> files,
      Principal principal) throws MSIException, IOException {
    var dto = new CreateInternshipApplicationDTO(companyId, note, principal.getName(), files);
    var responseData = InternshipApplicationDTO.getInstance(service.create(dto));
    return new ResponseEntity<>(responseData, HttpStatus.OK);
  }

  @PutMapping
  public ResponseEntity<InternshipApplicationDTO> update(
      @RequestParam("id") int id,
      @RequestParam("companyId") int companyId,
      @ModelAttribute("files") List<MultipartFile> files) {
    var dto = new UpdateInternshipApplicationDTO(id, companyId, files);
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

  @PutMapping("/verify")
  public ResponseEntity<Object> verify(@RequestBody @NonNull VerifyApplicationDTO dto) {
    service.verify(dto);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
