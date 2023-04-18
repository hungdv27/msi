package com.example.msi.controller;

import com.example.msi.models.result.CreateResultDTO;
import com.example.msi.models.result.ResultDTO;
import com.example.msi.service.ResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/result")

public class ResultController {
  private final ResultService service;

  @GetMapping("/{processId}")
  public ResponseEntity<Object> findByProcessId(@PathVariable int processId) {
    var entity = service.findByProcessId(processId);
    return entity.<ResponseEntity<Object>>
            map(result -> new ResponseEntity<>(ResultDTO.getInstance(result), HttpStatus.OK))
        .orElseGet(() -> new ResponseEntity<>(HttpStatus.OK));
  }

  @PostMapping
  public ResponseEntity<Object> createResult(@NonNull @RequestBody CreateResultDTO dto) {
    var responseData = ResultDTO.getInstance(service.create(dto));
    return new ResponseEntity<>(responseData, HttpStatus.OK);
  }
}
