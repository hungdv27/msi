package com.example.msi.controller;

import com.example.msi.domains.InternshipApplication;
import com.example.msi.domains.Student;
import com.example.msi.domains.Teacher;
import com.example.msi.domains.User;
import com.example.msi.models.error.ErrorDTO;
import com.example.msi.models.internshipprocess.AssignTeacherDTO;
import com.example.msi.models.internshipprocess.InternshipProcessDTO;
import com.example.msi.models.internshipprocess.InternshipProcessListDTO;
import com.example.msi.models.internshipprocess.SearchInternshipProcessDTO;
import com.example.msi.service.*;
import com.example.msi.shared.exceptions.ExceptionUtils;
import com.example.msi.shared.exceptions.MSIException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/internship-process")
@RequiredArgsConstructor
public class InternshipProcessController {
  private final InternshipProcessService service;
  private final StudentService studentService;
  private final InternshipApplicationService internshipApplicationService;
  private final TeacherService teacherService;
  private final UserService userService;

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
    var responseData = InternshipProcessDTO.getInstance(service.findById(id).orElseThrow());
    return new ResponseEntity<>(responseData, HttpStatus.OK);
  }

  @GetMapping("/me")
  public ResponseEntity<InternshipProcessDTO> findByMe(Principal principal) {
    var responseData = InternshipProcessDTO.getInstance(studentService.getInternshipProcess(principal.getName()));
    return new ResponseEntity<>(responseData, HttpStatus.OK);
  }

  @GetMapping("/search")
  public ResponseEntity<Page<InternshipProcessListDTO>> search(
      SearchInternshipProcessDTO searchDTO
  ) {
    // map các entity
    var internshipApplicationMap = internshipApplicationService.findAll().stream()
        .collect(Collectors.toMap(InternshipApplication::getId, ia -> ia));
    var studentMap = studentService.findAll().stream()
        .collect(Collectors.toMap(Student::getCode, s -> s));
    var userMap = userService.findAll().stream()
        .collect(Collectors.toMap(User::getId, u -> u));
    var teacherMap = teacherService.findAll().stream()
        .collect(Collectors.toMap(Teacher::getId, t -> t));

    var responseData = service.search(searchDTO)
        .map(entity -> InternshipProcessListDTO.getInstance(entity, internshipApplicationMap, studentMap, userMap, teacherMap));
    return new ResponseEntity<>(responseData, HttpStatus.OK);
  }

  @GetMapping(value = "/exportProcess", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> exportListProcess(
      @RequestParam(value = "studentCode", required = false) String studentCode,
      @RequestParam(value = "teacherId", required = false) Integer teacherId,
      @RequestParam(value = "semesterId", required = false) Integer semesterId,
      @RequestParam(value = "courseCode", required = false) String courseCode,
      HttpServletRequest request) {
    byte[] bytes;
    try {
      SearchInternshipProcessDTO reqDTO = new SearchInternshipProcessDTO(studentCode, teacherId, semesterId, courseCode);
      bytes = (byte[]) service.export(request, reqDTO);
      return new ResponseEntity<>(bytes, HttpStatus.OK);
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