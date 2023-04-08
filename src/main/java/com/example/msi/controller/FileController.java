package com.example.msi.controller;

import com.example.msi.models.file.FileDTO;
import com.example.msi.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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
  public List<FileDTO> uploadFiles(@RequestParam("files") List<MultipartFile> files) throws IOException {
    return service.uploadFiles(files).stream().map(FileDTO::getInstance).collect(Collectors.toList());
  }

  @GetMapping("/download")
  public ResponseEntity<byte[]> downloadFile(@RequestParam String fileKey) throws IOException {
    return service.downloadFile(fileKey);
  }
}
