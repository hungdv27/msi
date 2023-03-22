package com.example.msi.service;

import com.example.msi.models.file.CreateFileDTO;
import com.example.msi.response.Data;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
  Data uploadFile(CreateFileDTO createFileDTO, MultipartFile file);

  Data downloadFile(Integer fileId);
}
