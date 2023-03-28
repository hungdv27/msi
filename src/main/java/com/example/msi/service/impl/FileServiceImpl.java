package com.example.msi.service.impl;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.example.msi.domains.FileE;
import com.example.msi.repository.FileRepository;
import com.example.msi.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
  private final FileRepository repository;

  private AmazonS3 s3Client;

  @Value("${amazonProperties.endpointUrl}")
  private String endpointUrl;
  @Value("${amazonProperties.bucketName}")
  private String bucketName;
  @Value("${amazonProperties.accessKey}")
  private String accessKey;
  @Value("${amazonProperties.secretKey}")
  private String secretKey;

  @PostConstruct
  private void initializeAmazon() {
    AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
    this.s3Client = new AmazonS3Client(credentials);
  }

  private File convertMultiPartToFile(MultipartFile file) throws IOException {
    File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
    FileOutputStream fos = new FileOutputStream(convFile);
    fos.write(file.getBytes());
    fos.close();
    return convFile;
  }

  private String generateFileName(MultipartFile multiPart) {
    return new Date().getTime() + "-" + Objects.requireNonNull(multiPart.getOriginalFilename()).replace(" ", "_");
  }

  private void uploadFileTos3bucket(String fileName, File file) {
    s3Client.putObject(new PutObjectRequest(bucketName, fileName, file)
        .withCannedAcl(CannedAccessControlList.PublicRead));
  }

  @Override
  public String uploadFile(MultipartFile multipartFile) {
    var entity = new FileE();
    String fileUrl = "";
    try {
      File file = convertMultiPartToFile(multipartFile);
      String fileName = generateFileName(multipartFile);
      fileUrl = endpointUrl + "/" + fileName;
      uploadFileTos3bucket(fileName, file);
      entity.setFilename(fileName);
      file.delete();
    } catch (Exception e) {
      e.printStackTrace();
    }
    repository.save(entity);
    return fileUrl;
  }

  @Override
  public List<FileE> uploadFiles(List<MultipartFile> files) throws IOException {
    if (files.size() == 0) return null;
    List<FileE> entity = new ArrayList<>();
    for (MultipartFile file : files) {
      var newEntity = new FileE();
      String key = generateFileName(file);
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentType(file.getContentType());
      metadata.setContentLength(file.getSize());
      PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, file.getInputStream(), metadata);
      s3Client.putObject(putObjectRequest);
      newEntity.setFilename(key);
      entity.add(repository.save(newEntity));
    }
    return entity;
  }

  @Override
  public ResponseEntity<byte[]> downloadFile(String fileName) throws IOException {
    S3Object s3Object = s3Client.getObject(bucketName, fileName);
    byte[] content = s3Object.getObjectContent().readAllBytes();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
    headers.setContentDispositionFormData("attachment", fileName);
    headers.setContentLength(content.length);
    ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(content, headers, HttpStatus.OK);
    s3Object.close();
    return responseEntity;
  }

  @Override
  public List<FileE> findByIds(@NonNull List<Integer> ids) {
    return repository.findAllByIdIn(ids);
  }
}
