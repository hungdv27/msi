package com.example.msi.models.internshipappication;

import com.example.msi.domains.InternshipApplication;
import com.example.msi.models.company.CompanyDTO;
import com.example.msi.models.student.StudentDetailDTO;
import com.example.msi.shared.enums.InternshipApplicationStatus;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.msi.shared.utils.ServiceUtils.getCompanyService;
import static com.example.msi.shared.utils.ServiceUtils.getStudentService;

@Getter
public class InternshipApplicationDTO {
  private int id;
  private StudentDetailDTO student;
  private InternshipApplicationStatus status;
  private CompanyDTO company;
  private int semesterId;
  private String note;
  private LocalDateTime createdDate;
  private LocalDateTime updatedDate;
  private String courseCode;
  private String instructor;
  private String instructorContact;
  private String description;
  private LocalDate startDate;
  private LocalDate endDate;
  private Integer totalDayPerWeek;
  private Integer totalHourPerShift;

  private InternshipApplicationDTO(@NonNull InternshipApplication entity) {
    id = entity.getId();
    var stu = getStudentService().findByCode(entity.getStudentCode()).orElseThrow();
    student = new StudentDetailDTO(stu);
    status = entity.getStatus();
    semesterId = entity.getSemesterId();
    note = entity.getNote();
    createdDate = entity.getCreatedDate();
    updatedDate = entity.getUpdatedDate();
    courseCode = entity.getCourseCode();
    instructor = entity.getInstructor();
    instructorContact = entity.getInstructorContact();
    description = entity.getDescription();
    startDate = entity.getStartDate();
    endDate = entity.getEndDate();
    totalDayPerWeek = entity.getTotalDayPerWeek();
    totalHourPerShift = entity.getTotalHourPerShift();
    Optional.ofNullable(entity.getCompanyId()).ifPresent(
        value -> company = CompanyDTO.getInstance(getCompanyService().getCompanyById(entity.getCompanyId()))
    );
  }

  public static InternshipApplicationDTO getInstance(@NonNull InternshipApplication entity) {
    return new InternshipApplicationDTO(entity);
  }
}
