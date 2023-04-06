package com.example.msi.models.internshipappication;

import com.example.msi.domains.InternshipApplication;
import com.example.msi.domains.InternshipApplicationFile;
import com.example.msi.models.company.CompanyDTO;
import com.example.msi.models.file.FileDTO;
import com.example.msi.service.CompanyService;
import com.example.msi.service.FileService;
import com.example.msi.service.InternshipApplicationFileService;
import com.example.msi.shared.ApplicationContextHolder;
import com.example.msi.shared.enums.InternshipApplicationStatus;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public class InternshipApplicationDTO {
  private int id;
  private String studentCode;
  private InternshipApplicationStatus status;
  private List<FileDTO> files;
  private CompanyDTO company;
  private int semesterId;
  private String note;
  private LocalDateTime createdDate;
  private LocalDateTime updatedDate;
  private String courseName;
  private String instructor;
  private String instructorContact;
  private String description;
  private LocalDate startDate;
  private LocalDate endDate;
  private Integer totalDayPerWeek;
  private Integer totalHourPerShift;

  private InternshipApplicationDTO(@NonNull InternshipApplication entity) {
    id = entity.getId();
    studentCode = entity.getStudentCode();
    status = entity.getStatus();
    semesterId = entity.getSemesterId();
    note = entity.getNote();
    createdDate = entity.getCreatedDate();
    updatedDate = entity.getUpdatedDate();
    courseName = entity.getCourseName();
    instructor = entity.getInstructor();
    instructorContact = entity.getInstructorContact();
    description = entity.getDescription();
    startDate = entity.getStartDate();
    endDate = entity.getEndDate();
    totalDayPerWeek = entity.getTotalDayPerWeek();
    totalHourPerShift = entity.getTotalHourPerShift();
    Optional.ofNullable(entity.getCompanyId()).ifPresent(
        value -> company = CompanyDTO.getInstance(SingletonHelper.COMPANY_SERVICE.getCompanyById(entity.getCompanyId()))
    );
    var fileIds = SingletonHelper.INTERNSHIP_APPLICATION_FILE_SERVICE.findByInternshipApplicationId(entity.getId())
        .stream()
        .map(InternshipApplicationFile::getFileId)
        .collect(Collectors.toList());
    this.files = SingletonHelper.FILE_SERVICE.findByIds(fileIds).stream().map(FileDTO::getInstance).collect(Collectors.toList());
  }

  public static InternshipApplicationDTO getInstance(@NonNull InternshipApplication entity) {
    return new InternshipApplicationDTO(entity);
  }

  private static class SingletonHelper {
    private static final CompanyService COMPANY_SERVICE =
        ApplicationContextHolder.getBean(CompanyService.class);

    private static final InternshipApplicationFileService INTERNSHIP_APPLICATION_FILE_SERVICE =
        ApplicationContextHolder.getBean(InternshipApplicationFileService.class);

    private static final FileService FILE_SERVICE =
        ApplicationContextHolder.getBean(FileService.class);
  }
}

