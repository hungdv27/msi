package com.example.msi.controller;

import com.example.msi.domains.Teacher;
import com.example.msi.models.error.ErrorDTO;
import com.example.msi.models.student.UpdateStudentDTO;
import com.example.msi.models.teacher.SearchTeacherDTO;
import com.example.msi.models.teacher.TeacherDTO;
import com.example.msi.models.teacher.UpdateTeacherDTO;
import com.example.msi.models.user.SearchUserDTO;
import com.example.msi.models.user.UserDTO;
import com.example.msi.service.TeacherService;
import com.example.msi.service.UserService;
import com.example.msi.shared.exceptions.ExceptionUtils;
import com.example.msi.shared.exceptions.MSIException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@RequestMapping("api/teacher")
@RestController
public class TeacherController {
  private final TeacherService service;
  private final UserService userService;

  @GetMapping("/me")
  public ResponseEntity<Object> getStudent(Principal principal) {
    try {
      var userName = principal.getName();
      return new ResponseEntity<>(service.findByUsername(userName), HttpStatus.OK);
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
  public ResponseEntity<Object> updateTeacher(@RequestBody UpdateTeacherDTO payload, Principal principal) {
    try {
      var userName = principal.getName();
      service.updateTeacher(payload, userName);
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

  @GetMapping("/search")
  public ResponseEntity<Page<TeacherDTO>> search(
      SearchTeacherDTO searchDTO
  ) throws MSIException {
    var responseData = service.search(searchDTO).map(
        teacher -> {
          var userId = teacher.getUserId();
          var user = userService.findById(userId).orElse(null);
          return TeacherDTO.getInstance(teacher,user);
        }
    );
    return new ResponseEntity<>(responseData, HttpStatus.OK);
  }

  @PutMapping("/change-status/{id}")
  public ResponseEntity<Object> changeStatus(@NonNull @PathVariable int id) {
    try {
      service.changeStatus(id);
      return new ResponseEntity<>(HttpStatus.ACCEPTED);
    } catch (Exception ex) {
      return new ResponseEntity<>(
          ExceptionUtils.messages.get(ExceptionUtils.E_INTERNAL_SERVER),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
