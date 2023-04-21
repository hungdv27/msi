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
  private final int id;
  private final StudentDetailDTO student;
  private final InternshipApplicationStatus status;
  private final int semesterId;
  private final String note;
  private final LocalDateTime createdDate;
  private final LocalDateTime updatedDate;
  private final String courseCode;
  private final String instructor;
  private final String instructorContact;
  private final String description;
  private final LocalDate startDate;
  private final LocalDate endDate;
  private final Integer totalDayPerWeek;
  private final Integer totalHourPerShift;
  private CompanyDTO company;

  private InternshipApplicationDTO(@NonNull InternshipApplication entity) {
    id = entity.getId();
    var stuOptional = getStudentService().findByCode(entity.getStudentCode());
    if (stuOptional.isEmpty())
      throw new RuntimeException("Không tìm thấy dữ liệu sinh viên " + entity.getStudentCode());
    var stu = stuOptional.get();
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
    Optional.ofNullable(entity.getCompanyId()).ifPresent(value -> company = CompanyDTO.getInstance(getCompanyService().getCompanyById(entity.getCompanyId())));
  }

  public static InternshipApplicationDTO getInstance(@NonNull InternshipApplication entity) {
    return new InternshipApplicationDTO(entity);
  }
}
