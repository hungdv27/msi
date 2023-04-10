package com.example.msi.models.internshipprocess;

import com.example.msi.domains.InternshipProcess;
import com.example.msi.models.company.CompanyDTO;
import com.example.msi.models.report.ReportDTO;
import com.example.msi.service.*;
import com.example.msi.shared.ApplicationContextHolder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
public class InternshipProcessDTO {
  private int id;
  private String studentCode;
  private CompanyDTO company;
  private List<ReportDTO> reports;
  private String teacherName;
  private LocalDate startDate;
  private LocalDate endDate;
  private Integer currentWeek;

  private InternshipProcessDTO(@NonNull InternshipProcess entity) {
    var appId = entity.getApplicationId();
    var internshipApplication = SingletonHelper.INTERNSHIP_APPLICATION_SERVICE.findById(appId);

    // id
    id = entity.getId();
    // studentCode
    studentCode = internshipApplication.getStudentCode();
    // company
    Optional.ofNullable(internshipApplication.getCompanyId()).ifPresent(
        value -> company = CompanyDTO.getInstance(InternshipProcessDTO.SingletonHelper.COMPANY_SERVICE.getCompanyById(internshipApplication.getCompanyId()))
    );
    // startDate
    startDate = internshipApplication.getStartDate();
    // endDate
    endDate = internshipApplication.getEndDate();
    // reports
    var reportList = SingletonHelper.REPORT_SERVICE.findAllByProcessId(entity.getId());
    var reportDTOList = reportList.stream().map(ReportDTO::getInstance).collect(Collectors.toList());
    reports = new ArrayList<>();
    reports.addAll(reportDTOList);
  }

  public static InternshipProcessDTO getInstance(@NonNull InternshipProcess entity) {
    return new InternshipProcessDTO(entity);
  }

  private static class SingletonHelper {
    private static final InternshipApplicationService INTERNSHIP_APPLICATION_SERVICE =
        ApplicationContextHolder.getBean(InternshipApplicationService.class);

    private static final CompanyService COMPANY_SERVICE =
        ApplicationContextHolder.getBean(CompanyService.class);

    private static final ReportService REPORT_SERVICE =
        ApplicationContextHolder.getBean(ReportService.class);

    private static final InternshipApplicationFileService INTERNSHIP_APPLICATION_FILE_SERVICE =
        ApplicationContextHolder.getBean(InternshipApplicationFileService.class);

    private static final FileService FILE_SERVICE =
        ApplicationContextHolder.getBean(FileService.class);
  }
}
