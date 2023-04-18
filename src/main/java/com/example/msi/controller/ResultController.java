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
  public ResponseEntity<Object> findByProcessId(@PathVariable int processId){
    var responseData = ResultDTO.getInstance(service.findByProcessId(processId).orElseThrow());
    return new ResponseEntity<>(responseData, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<Object> createResult(@NonNull @RequestBody CreateResultDTO dto){
    var responseData = ResultDTO.getInstance(service.create(dto));
    return new ResponseEntity<>(responseData, HttpStatus.OK);
  }
}
