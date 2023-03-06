package com.example.msi.controller;

import com.example.msi.models.company.CreateMajorDTO;
import com.example.msi.models.company.UpdateMajorDTO;
import com.example.msi.service.MajorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/major")
@RestController
public class MajorController {
  private final MajorService service;

  @GetMapping("")
  public ResponseEntity<Object> getAllMajor() {
    return new ResponseEntity<>(service.getAllMajor(), HttpStatus.OK);
  }

  @PostMapping("")
  public ResponseEntity<Object> createMajor(@RequestBody CreateMajorDTO major) {
    service.addMajor(major);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PutMapping("")
  public ResponseEntity<Object> updateMajor(@RequestBody UpdateMajorDTO payload) {
    service.updateMajor(payload);
    return new ResponseEntity<>(HttpStatus.ACCEPTED);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteMajor(@PathVariable int id) {
    service.deleteMajor(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
