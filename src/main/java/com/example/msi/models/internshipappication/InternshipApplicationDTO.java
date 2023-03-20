package com.example.msi.models.internshipappication;

import com.example.msi.domains.InternshipApplication;
import com.example.msi.models.company.CompanyDTO;
import com.example.msi.service.CompanyService;
import com.example.msi.shared.ApplicationContextHolder;
import com.example.msi.shared.enums.InternshipApplicationStatus;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
public class InternshipApplicationDTO {
  private int id;
  private String studentCode;
  private InternshipApplicationStatus status;
  private Integer fileId;
  private CompanyDTO company;
  private int semesterId;
  private String note;
  private LocalDateTime createdDate;
  private LocalDateTime updatedDate;

  private InternshipApplicationDTO(@NonNull InternshipApplication entity) {
    id = entity.getId();
    studentCode = entity.getStudentCode();
    status = entity.getStatus();
    fileId = entity.getFileId();
    semesterId = entity.getSemesterId();
    note = entity.getNote();
    createdDate = entity.getCreatedDate();
    updatedDate = entity.getUpdatedDate();
    Optional.ofNullable(entity.getCompanyId()).ifPresent(value -> company = CompanyDTO.getInstance(
        SingletonHelper.COMPANY_SERVICE.getCompanyById(entity.getCompanyId()))
    );
  }

  public static InternshipApplicationDTO getInstance(@NonNull InternshipApplication entity) {
    return new InternshipApplicationDTO(entity);
  }

  private static class SingletonHelper {
    private static final CompanyService COMPANY_SERVICE =
        ApplicationContextHolder.getBean(CompanyService.class);
  }

}

