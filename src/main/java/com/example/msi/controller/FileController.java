package com.example.msi.controller;

import com.example.msi.models.file.CreateFileDTO;
import com.example.msi.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("api/file")
@RestController
@Slf4j
public class FileController {
  private final FileService service;

  @PostMapping("/uploadFile")
  public String uploadFile(@RequestPart(value = "file") MultipartFile file) {
    return this.service.uploadFile(file);
  }
}
