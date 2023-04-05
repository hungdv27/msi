package com.example.msi.service.impl;

import com.example.msi.domains.FileE;
import com.example.msi.domains.InternshipApplication;
import com.example.msi.models.internshipappication.CreateInternshipApplicationDTO;
import com.example.msi.models.internshipappication.SearchInternshipApplicationDTO;
import com.example.msi.models.internshipappication.UpdateInternshipApplicationDTO;
import com.example.msi.models.internshipappication.VerifyApplicationDTO;
import com.example.msi.models.internshipapplication_file.CreateInternshipApplicationFileDTO;
import com.example.msi.repository.InternshipApplicationRepository;
import com.example.msi.service.FileService;
import com.example.msi.service.InternshipApplicationFileService;
import com.example.msi.service.InternshipApplicationService;
import com.example.msi.service.StudentService;
import com.example.msi.shared.exceptions.MSIException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InternshipApplicationServiceImpl implements InternshipApplicationService {
  private final InternshipApplicationRepository repository;
  private final FileService fileService;
  private final InternshipApplicationFileService internshipApplicationFileService;
  private final StudentService studentService;

  @Override
  public Page<InternshipApplication> search(@NonNull SearchInternshipApplicationDTO filter) {
    var spec = filter.getSpecification();
    var pageable = filter.getPageable();
    return repository.findAll(spec, pageable);
  }

  @Override
  public InternshipApplication findById(int id) {
    return repository.findById(id).orElseThrow(NoSuchElementException::new);
  }

  @Override
  public List<InternshipApplication> findByUsername(@NonNull String username) throws MSIException {
    var student = studentService.findByUsername(username).orElseThrow(NoSuchElementException::new);
    return repository.findAllByStudentCode(student.getCode());
  }

  @Override
  public InternshipApplication create(@NonNull CreateInternshipApplicationDTO dto) throws MSIException, IOException {
    var entity = repository.save(InternshipApplication.getInstance(dto));
    attachFiles(entity.getId(), dto.getFiles());
    return entity;
  }

  @Override
  @Transactional
  public Optional<InternshipApplication> update(@NonNull UpdateInternshipApplicationDTO dto) {
    return repository.findById(dto.getId()).map(entity -> {
      entity.update(dto);
      if (dto.getFiles() != null) {
        unattachedFiles(entity.getId());
      }
      try {
        attachFiles(entity.getId(), dto.getFiles());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      return repository.save(entity);
    });
  }

  @Override
  public void delete(int id) {
    internshipApplicationFileService.deleteByInternshipApplicationId(id);
    repository.deleteById(id);
  }

  @Override
  @Transactional
  public void verify(@NonNull VerifyApplicationDTO dto) {
    repository.findById(dto.getId()).ifPresent(entity -> entity.update(dto));
  }

  private void attachFiles(int internshipApplicationFileId, List<MultipartFile> multipartFiles) throws IOException {
    if (multipartFiles == null) return;
    var files = fileService.uploadFiles(multipartFiles);
    for (FileE file : files) {
      var iaf = CreateInternshipApplicationFileDTO.getInstance(internshipApplicationFileId, file.getId());
      internshipApplicationFileService.add(iaf);
    }
  }

  private void unattachedFiles(int internshipApplicationId) {
    internshipApplicationFileService.deleteByInternshipApplicationId(internshipApplicationId);
  }
}
