package com.example.msi.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.msi.domains.File;
import com.example.msi.models.file.CreateFileDTO;
import com.example.msi.repository.FileRepository;
import com.example.msi.response.Data;
import com.example.msi.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
  private final FileRepository repository;
  private final Cloudinary cloudinary;

  @Override
  public Data uploadFile(CreateFileDTO createFileDTO, MultipartFile file) {
    Optional.ofNullable(file)
        .orElseThrow(() -> new IllegalArgumentException("File cannot be null"));

    File entity = File.getInstance(createFileDTO);
    try {
      Map<String, Object> uploadResult = this.cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
      entity.setFileURL(uploadResult.get("url").toString());
    } catch (Exception e) {
      log.error("Error uploading file: " + e.getMessage());
    }

    repository.save(entity);
    return new Data(entity);
  }

  @Override
  public Data downloadFile(Integer fileId) {

    return null;
  }

}
