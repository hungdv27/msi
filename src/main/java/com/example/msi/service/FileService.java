package com.example.msi.service;

import com.example.msi.domains.FileE;
import com.example.msi.models.file.CreateFileDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.msi.response.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FileService {
  String uploadFile(MultipartFile multipartFile);

  ResponseEntity<byte[]> downloadFile(String fileName) throws IOException;

  List<FileE> uploadFiles(List<MultipartFile> files) throws IOException;
}
