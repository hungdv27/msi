package com.example.msi.service;

import com.example.msi.domains.FileE;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
  String uploadFile(MultipartFile multipartFile);

  ResponseEntity<byte[]> downloadFile(String fileName) throws IOException;

  List<FileE> uploadFiles(List<MultipartFile> files) throws IOException;

  List<FileE> findByIds(@NonNull List<Integer> ids);

  void deleteByIds(@NonNull List<Integer> ids);
  void deleteByUId(int id);
}
