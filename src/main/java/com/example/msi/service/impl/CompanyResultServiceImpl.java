package com.example.msi.service.impl;

import com.example.msi.domains.CompanyResult;
import com.example.msi.domains.CompanyResultFile;
import com.example.msi.domains.FileE;
import com.example.msi.domains.ReportFile;
import com.example.msi.models.companyresult.CreateCompanyResultDTO;
import com.example.msi.models.companyresult_file.CreateCompanyResultFileDTO;
import com.example.msi.repository.CompanyResultRepository;
import com.example.msi.service.CompanyResultFileService;
import com.example.msi.service.CompanyResultService;
import com.example.msi.service.FileService;
import com.example.msi.shared.exceptions.MSIException;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyResultServiceImpl implements CompanyResultService {
  private final CompanyResultRepository repository;
  private final FileService fileService;
  private final CompanyResultFileService companyResultFileService;


  @Override
  @Transactional
  public CompanyResult create(@NonNull CreateCompanyResultDTO dto) throws MSIException, IOException {
    // check trùng studentCode
    var optional = repository.findTopByStudentCode(dto.getStudentCode());
    optional.ifPresent(cr -> delete(cr.getId()));

    var entity = repository.save(CompanyResult.getInstance(dto));
    attachFiles(entity.getId(), dto.getFiles());
    return entity;
  }

  @Override
  @Transactional
  public void delete(int id) {
    var fileIds = companyResultFileService.findByCompanyResultId(id)
        .stream()
        .map(CompanyResultFile::getFileId)
        .collect(Collectors.toList());
    companyResultFileService.deleteByCompanyResultId(id);
    fileService.deleteByIds(fileIds);
    repository.deleteById(id);
  }

  @Override
  public Optional<CompanyResult> findByStudentCode(String code) {
    return repository.findTopByStudentCode(code);
  }

  @Override
  public List<CompanyResult> findAll() {
    return repository.findAll();
  }

  private void attachFiles(int companyResultFileId, List<MultipartFile> multipartFiles) throws IOException {
    if (multipartFiles == null) return;
    var files = fileService.uploadFiles(multipartFiles);
    for (FileE file : files) {
      var iaf = CreateCompanyResultFileDTO.getInstance(companyResultFileId, file.getId());
      companyResultFileService.add(iaf);
    }
  }
}
