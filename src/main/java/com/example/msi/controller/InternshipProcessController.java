package com.example.msi.controller;

import com.example.msi.models.error.ErrorDTO;
import com.example.msi.models.internshipprocess.AssignTeacherDTO;
import com.example.msi.models.internshipprocess.InternshipProcessDTO;
import com.example.msi.models.internshipprocess.SearchInternshipProcessDTO;
import com.example.msi.service.InternshipProcessService;
import com.example.msi.service.StudentService;
import com.example.msi.shared.exceptions.ExceptionUtils;
import com.example.msi.shared.exceptions.MSIException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/internship-process")
@RequiredArgsConstructor
public class InternshipProcessController {
  private final InternshipProcessService service;
  private final StudentService studentService;

  @PutMapping("/assignTeacher")
  public ResponseEntity<Object> assignTeacher(@RequestBody AssignTeacherDTO dto, Principal principal) {
    try {
      service.assignTeacher(dto, principal.getName());
    } catch (MSIException ex) {
      return new ResponseEntity<>(
          new ErrorDTO(ex.getMessageKey(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    } catch (Exception ex) {
      return new ResponseEntity<>(
          ExceptionUtils.messages.get(ExceptionUtils.E_INTERNAL_SERVER),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<>(HttpStatus.ACCEPTED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<InternshipProcessDTO> findById(@PathVariable @NonNull int id) {
    var responseData = InternshipProcessDTO.getInstance(service.findById(id));
    return new ResponseEntity<>(responseData, HttpStatus.OK);
  }

  @GetMapping("/me")
  public ResponseEntity<InternshipProcessDTO> findByMe(Principal principal) {
    var responseData = InternshipProcessDTO.getInstance(studentService.getInternshipProcess(principal.getName()));
    return new ResponseEntity<>(responseData, HttpStatus.OK);
  }

  @GetMapping("/search")
  public ResponseEntity<Page<InternshipProcessDTO>> search(
      SearchInternshipProcessDTO searchDTO
  ) {
    var responseData = service.search(searchDTO)
        .map(InternshipProcessDTO::getInstance);
    return new ResponseEntity<>(responseData, HttpStatus.OK);
  }
}