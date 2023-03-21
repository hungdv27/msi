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

  @PostMapping("")
  public ResponseEntity<?> uploadFile(
      @ModelAttribute CreateFileDTO createFileDTO,
      @RequestParam(name = "file", required = false) MultipartFile file) {
    return new ResponseEntity<>(service.uploadFile(createFileDTO, file), HttpStatus.CREATED);
  }

  @PostMapping("download_file/{fileId}")
  public ResponseEntity<?> downloadFile(@PathVariable Integer fileId) {
    return new ResponseEntity<>(service.downloadFile(fileId), HttpStatus.OK);
  }
}
