package com.example.msi.controller;

import com.example.msi.models.company.CreateSemesterDTO;
import com.example.msi.models.company.UpdateSemesterDTO;
import com.example.msi.service.SemesterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/semester")
@RestController
public class SemesterController {
  private final SemesterService service;

  @GetMapping("")
  public ResponseEntity<Object> getAllSemester() {
    return new ResponseEntity<>(service.getAllSemester(), HttpStatus.OK);
  }

  @PostMapping("")
  public ResponseEntity<Object> createSemester(@RequestBody CreateSemesterDTO major) {
    service.addSemester(major);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PutMapping("")
  public ResponseEntity<Object> updateSemester(@RequestBody UpdateSemesterDTO payload) {
    service.updateSemester(payload);
    return new ResponseEntity<>(HttpStatus.ACCEPTED);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteSemester(@PathVariable int id) {
    service.deleteSemester(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
