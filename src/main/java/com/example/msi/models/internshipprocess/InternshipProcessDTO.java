package com.example.msi.models.internshipprocess;

import com.example.msi.domains.InternshipProcess;
import com.example.msi.models.company.CompanyDTO;
import com.example.msi.models.report.ReportDTO;
import com.example.msi.models.student.StudentDetailDTO;
import com.example.msi.models.teacher.TeacherDTO;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.msi.shared.utils.ServiceUtils.*;

@Getter
@Setter
public class InternshipProcessDTO {
  private int id;
  private StudentDetailDTO student;
  private TeacherDTO teacher;
  private CompanyDTO company;
  private List<ReportDTO> reports;
  private int applicationId;
  private LocalDate startDate;
  private LocalDate endDate;
  private Long currentWeek;

  private InternshipProcessDTO(@NonNull InternshipProcess entity) {
    var appId = entity.getApplicationId();
    var internshipApplication = getInternshipApplicationService().findById(appId);

    // id
    id = entity.getId();
    // student
    Optional.ofNullable(internshipApplication.getStudentCode()).ifPresent(
        value -> student = StudentDetailDTO.getInstance(getStudentService().findByCode(internshipApplication.getStudentCode()).orElseThrow())
    );
    // teacher
    Optional.ofNullable(entity.getTeacherId()).ifPresent(
        value -> {
          var userId = getTeacherService().findById(entity.getTeacherId()).orElseThrow().getUserId();
          var user = getUserService().findById(userId).orElseThrow();
          teacher = TeacherDTO
              .getInstance(getTeacherService().findById(entity.getTeacherId()).orElseThrow(), user);
        }
    );
    // company
    Optional.ofNullable(internshipApplication.getCompanyId()).ifPresent(
        value -> company = CompanyDTO.getInstance(getCompanyService().getCompanyById(internshipApplication.getCompanyId()))
    );
    // startDate
    startDate = internshipApplication.getStartDate();
    // endDate
    endDate = internshipApplication.getEndDate();
    // reports
    var reportList = getReportService().findAllByProcessId(entity.getId());
    var reportDTOList = reportList.stream().map(ReportDTO::getInstance).collect(Collectors.toList());
    reports = new ArrayList<>();
    reports.addAll(reportDTOList);
    // applicationId
    applicationId = entity.getApplicationId();
    // currentWeek
    currentWeek = getInternshipProcessService().currentWeekProcess(internshipApplication);
  }

  public static InternshipProcessDTO getInstance(@NonNull InternshipProcess entity) {
    return new InternshipProcessDTO(entity);
  }
}
