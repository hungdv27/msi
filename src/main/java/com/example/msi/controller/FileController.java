package com.example.msi.controller;

import com.example.msi.domains.FileE;
import com.example.msi.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

  @PostMapping("/uploadFiles")
  public List<FileE> uploadFiles(@RequestParam("files") List<MultipartFile> files) throws IOException {
    return service.uploadFiles(files);
  }

  @GetMapping("/download")
  public ResponseEntity<byte[]> downloadFile(@RequestParam String fileName) throws IOException {
    return service.downloadFile(fileName);
  }

}
