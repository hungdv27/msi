package com.example.msi.controller;

import com.example.msi.models.error.ErrorDTO;
import com.example.msi.models.student.UpdateStudentDTO;
import com.example.msi.service.StudentService;
import com.example.msi.shared.exceptions.ExceptionUtils;
import com.example.msi.shared.exceptions.MSIException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("api/student")
@RestController
public class StudentController {
  private final StudentService service;

  @GetMapping("")
  public ResponseEntity<Object> getStudent(Principal principal) {
    try {
      var userName = principal.getName();
      return new ResponseEntity<>(service.findByUserId(userName), HttpStatus.OK);
    } catch (MSIException ex) {
      return new ResponseEntity<>(
          new ErrorDTO(ex.getMessageKey(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    } catch (Exception ex) {
      return new ResponseEntity<>(
          ExceptionUtils.messages.get(ExceptionUtils.E_INTERNAL_SERVER),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("")
  public ResponseEntity<Object> updateStudent(@RequestBody UpdateStudentDTO payload, Principal principal) {
    try {
      var userName = principal.getName();
      service.updateStudent(payload, userName);
      return new ResponseEntity<>(HttpStatus.ACCEPTED);
    } catch (MSIException ex) {
      return new ResponseEntity<>(
          new ErrorDTO(ex.getMessageKey(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    } catch (Exception ex) {
      return new ResponseEntity<>(
          ExceptionUtils.messages.get(ExceptionUtils.E_INTERNAL_SERVER),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
