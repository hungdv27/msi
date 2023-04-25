package com.example.msi.models.internshipprocess;

import com.example.msi.domains.InternshipProcess;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.time.LocalDate;

import static com.example.msi.shared.utils.ServiceUtils.*;

@Getter
@Setter
public class InternshipProcessListDTO {
  private int id;
  private String studentCode;
  private String studentFullName;
  private String teacherFullName;
  private Long currentWeek;
  private LocalDate startDate;
  private LocalDate endDate;

  private InternshipProcessListDTO(@NonNull InternshipProcess entity) {
    var appId = entity.getApplicationId();
    var internshipApplication = getInternshipApplicationService().findById(appId);
    // id
    id = entity.getId();
    // studentCode
    studentCode = internshipApplication.getStudentCode();
    // studentFullName
    var userIdStudent = getStudentService().findByCode(studentCode).orElseThrow().getUserId();
    studentFullName = getUserService().findById(userIdStudent).orElseThrow().getFullName();
    // teacherFullName
    var userIdTeacher = getTeacherService().findById(entity.getTeacherId()).orElseThrow().getUserId();
    teacherFullName = getUserService().findById(userIdTeacher).orElseThrow().getFullName();
    // currentWeek
    currentWeek = getInternshipProcessService().currentWeekProcess(internshipApplication);
    // startDate
    startDate = internshipApplication.getStartDate();
    // endDate
    endDate = internshipApplication.getEndDate();
  }

  public static InternshipProcessListDTO getInstance(@NonNull InternshipProcess entity) {
    return new InternshipProcessListDTO(entity);
  }
}
