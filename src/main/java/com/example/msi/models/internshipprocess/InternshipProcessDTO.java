package com.example.msi.models.internshipprocess;

import com.example.msi.domains.InternshipProcess;
import com.example.msi.models.company.CompanyDTO;
import com.example.msi.models.report.ReportDTO;
import com.example.msi.service.*;
import com.example.msi.shared.ApplicationContextHolder;
import com.example.msi.shared.utils.Utils;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
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
  private Long currentWeek;

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
    // currentWeek
    var checkCreatedDate = entity.getCreatedDate().toLocalDate();
    var checkStartDate = internshipApplication.getStartDate();
    var daysBetween = ChronoUnit.DAYS.between(checkStartDate, checkCreatedDate);
    currentWeek = Utils.checkCurrentWeek(daysBetween);
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
  }

//  public Map<String, Integer> dayAndNumberWeekMap(){
//    Map<String, Integer> hashMap = new HashMap<>();
//
//    return hashMap;
//  }
}
