package com.example.msi.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.example.msi.domains.File;
import com.example.msi.models.file.CreateFileDTO;
import com.example.msi.repository.FileRepository;
import com.example.msi.response.Data;
import com.example.msi.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
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
      Map<String, Object> options = new HashMap<>();
      options.put("public_id", file.getOriginalFilename());
      options.put("resource_type", "auto");
      Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);
      entity.setFileURL(uploadResult.get("url").toString());
      entity.setPublicFileId(uploadResult.get("public_id").toString());
    } catch (Exception e) {
      log.error("Error uploading file: " + e.getMessage());
    }

    repository.save(entity);
    return new Data(entity);
  }

  @Override
  public Data downloadFile(Integer fileId) {
    var file = repository.findById(fileId);
//    var url = file.get().getFileURL();
//    try {
//      Map<String, Object> options = ObjectUtils.asMap(
//          "resource_type", "auto",
//          "fetch_format", "docx"
//      );
//      String transformedUrl = cloudinary.url().transformation(options).generate(url);
//
//      InputStream in = new URL(transformedUrl).openStream();
//      Files.copy(in, Paths.get(url.substring(url.lastIndexOf("/") + 1) + ".docx"));
//    } catch (IOException e) {
//      e.printStackTrace();
//    }

    return new Data(file);
  }

}
