package com.example.msi.service.impl;

import com.example.msi.domains.FileE;
import com.example.msi.domains.InternshipApplication;
import com.example.msi.domains.InternshipApplicationFile;
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
import com.example.msi.shared.enums.InternshipApplicationStatus;
import com.example.msi.shared.exceptions.MSIException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.msi.shared.enums.InternshipApplicationStatus.NEW;
import static com.example.msi.shared.enums.InternshipApplicationStatus.WAITING;

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
    var student = studentService.findByUsername(username)
        .orElseThrow(NoSuchElementException::new);
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
      if (dto.getExistedFiles() != null) {
        List<Integer> removeFileIds = new ArrayList<>();
        var fileIds = internshipApplicationFileService.findByInternshipApplicationId(dto.getId())
            .stream().map(InternshipApplicationFile::getFileId).collect(Collectors.toList());
        var fileCurrents = fileService.findByIds(fileIds);
        for (FileE file : fileCurrents) {
          if (!dto.getExistedFiles().contains(file.getFileKey())) {
            removeFileIds.add(file.getId());
          }
        }
        unattachedFiles(removeFileIds);
      }
      try {
        attachFiles(entity.getId(), dto.getFileNews());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      return repository.save(entity);
    });
  }

  @Override
  @Transactional
  public void delete(int id) {
    var fileIds = internshipApplicationFileService.findByInternshipApplicationId(id)
        .stream()
        .map(InternshipApplicationFile::getFileId)
        .collect(Collectors.toList());
    internshipApplicationFileService.deleteByInternshipApplicationId(id);
    fileService.deleteByIds(fileIds);
    repository.deleteById(id);
  }

  @Override
  @Transactional
  public void verify(@NonNull VerifyApplicationDTO dto) {
    repository.findById(dto.getId())
        .ifPresent(entity -> entity.verify(dto));
  }

  @Override
  @Transactional
  public Optional<InternshipApplication> regis(int id) {
    return repository.findById(id).map(ia -> {
      if (repository.existsByStudentCodeAndStatus(ia.getStudentCode(), WAITING)) {
        throw new RuntimeException("Đã tồn tại yêu cầu duyệt đơn thực tập");
      }
      if (ia.getStatus() == NEW)
        ia.setStatus(WAITING);
      return repository.save(ia);
    });
  }

  @Override
  @Transactional
  public Optional<InternshipApplication> cancelRegis(int id) {
    return repository.findById(id).map(ia -> {
      if (ia.getStatus() == WAITING)
        ia.setStatus(NEW);
      return repository.save(ia);
    });
  }

  @Override
  public Optional<InternshipApplication> findByStudentCodeAndStatus(String studentCode, InternshipApplicationStatus status) {
    return repository.findTopByStudentCodeAndStatus(studentCode, status);
  }

  private void attachFiles(int internshipApplicationFileId, List<MultipartFile> multipartFiles) throws IOException {
    if (multipartFiles == null) return;
    var files = fileService.uploadFiles(multipartFiles);
    for (FileE file : files) {
      var iaf = CreateInternshipApplicationFileDTO.getInstance(internshipApplicationFileId, file.getId());
      internshipApplicationFileService.add(iaf);
    }
  }

  private void unattachedFiles(@NonNull List<Integer> fileIds) {
    internshipApplicationFileService.deleteByFileIds(fileIds);
    fileService.deleteByIds(fileIds);
  }
}
