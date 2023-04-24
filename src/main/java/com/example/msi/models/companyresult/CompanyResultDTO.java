package com.example.msi.models.companyresult;

import com.example.msi.domains.CompanyResult;
import com.example.msi.domains.CompanyResultFile;
import com.example.msi.models.file.FileDTO;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.msi.shared.utils.ServiceUtils.*;

@Getter
public class CompanyResultDTO {
  private final int id;
  private final Float companyGrade;
  private final String companyReview;
  private final String studentCode;
  private final List<FileDTO> files;

  private CompanyResultDTO(@NonNull CompanyResult entity) {
    this.id = entity.getId();
    this.companyGrade = entity.getCompanyGrade();
    this.companyReview = entity.getCompanyReview();
    this.studentCode = entity.getStudentCode();
    var fileIds = getCompanyResultFileService()
        .findByCompanyResultId(entity.getId())
        .stream()
        .map(CompanyResultFile::getFileId)
        .collect(Collectors.toList());
    this.files = getFileService()
        .findByIds(fileIds).stream().map(FileDTO::getInstance).collect(Collectors.toList());
  }

  public static CompanyResultDTO getInstance(@NonNull CompanyResult entity) {
    return new CompanyResultDTO(entity);
  }
}
