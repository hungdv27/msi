package com.example.msi.models.internshipprocess;

import com.example.msi.domains.InternshipProcess;
import com.example.msi.models.company.CompanyDTO;
import com.example.msi.models.report.ReportDTO;
import com.example.msi.models.student.StudentDetailDTO;
import com.example.msi.service.*;
import com.example.msi.shared.ApplicationContextHolder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
public class InternshipProcessDTO {
  private int id;
  private StudentDetailDTO student;
  private CompanyDTO company;
  private List<ReportDTO> reports;
  private int applicationId;
  private int teacherId;
  private String teacherName;
  private LocalDate startDate;
  private LocalDate endDate;
  private Long currentWeek;

  private InternshipProcessDTO(@NonNull InternshipProcess entity) {
    var appId = entity.getApplicationId();
    var internshipApplication = SingletonHelper.INTERNSHIP_APPLICATION_SERVICE.findById(appId);

    // id
    id = entity.getId();
    // student
    Optional.ofNullable(internshipApplication.getStudentCode()).ifPresent(
        value -> student = StudentDetailDTO.getInstance(InternshipProcessDTO.SingletonHelper.STUDENT_SERVICE.findByCode(internshipApplication.getStudentCode()).orElseThrow())
    );
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
    // applicationId
    applicationId = entity.getApplicationId();
    // teacherId, teacherName
    if (entity.getTeacherId() != null) {
      teacherId = entity.getTeacherId();
      teacherName = SingletonHelper.TEACHER_SERVICE.findById(entity.getTeacherId()).orElseThrow().getName();
    }
    // currentWeek
    currentWeek = SingletonHelper.INTERNSHIP_PROCESS_SERVICE.currentWeekProcess(internshipApplication);
  }

  public static InternshipProcessDTO getInstance(@NonNull InternshipProcess entity) {
    return new InternshipProcessDTO(entity);
  }

  private static class SingletonHelper {
    private static final InternshipApplicationService INTERNSHIP_APPLICATION_SERVICE =
        ApplicationContextHolder.getBean(InternshipApplicationService.class);
    private static final InternshipProcessService INTERNSHIP_PROCESS_SERVICE =
        ApplicationContextHolder.getBean(InternshipProcessService.class);
    private static final StudentService STUDENT_SERVICE =
        ApplicationContextHolder.getBean(StudentService.class);
    private static final CompanyService COMPANY_SERVICE =
        ApplicationContextHolder.getBean(CompanyService.class);
    private static final ReportService REPORT_SERVICE =
        ApplicationContextHolder.getBean(ReportService.class);
    private static final TeacherService TEACHER_SERVICE =
        ApplicationContextHolder.getBean(TeacherService.class);
  }
}
