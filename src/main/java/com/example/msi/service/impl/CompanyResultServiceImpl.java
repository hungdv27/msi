package com.example.msi.service.impl;

import com.example.msi.domains.CompanyResult;
import com.example.msi.domains.FileE;
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

@Service
@RequiredArgsConstructor
public class CompanyResultServiceImpl implements CompanyResultService {
  private final CompanyResultRepository repository;
  private final FileService fileService;
  private final CompanyResultFileService companyResultFileService;


  @Override
  @Transactional
  public CompanyResult create(@NonNull CreateCompanyResultDTO dto) throws MSIException, IOException {
    var entity = repository.save(CompanyResult.getInstance(dto));
    attachFiles(entity.getId(), dto.getFiles());
    return entity;
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
