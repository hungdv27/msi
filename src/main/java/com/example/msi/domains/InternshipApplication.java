package com.example.msi.domains;

import com.example.msi.models.internshipappication.CreateInternshipApplicationDTO;
import com.example.msi.models.internshipappication.UpdateInternshipApplicationDTO;
import com.example.msi.models.internshipappication.VerifyApplicationDTO;
import com.example.msi.shared.enums.InternshipApplicationStatus;
import com.example.msi.shared.exceptions.MSIException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.msi.shared.enums.InternshipApplicationStatus.WAITING;
import static com.example.msi.shared.utils.ServiceUtils.getSemesterService;
import static com.example.msi.shared.utils.ServiceUtils.getStudentService;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Table(name = "internship_application")
public class InternshipApplication {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "student_code", length = 50)
  private String studentCode;

  @Column(name = "status", nullable = false)
  @Enumerated
  private InternshipApplicationStatus status;

  @Column(name = "company_id")
  private Integer companyId;

  @Column(name = "semester_id", nullable = false)
  private int semesterId;

  @Column(name = "note", length = 10000)
  private String note;

  @Column(name = "course_code", length = 50)
  private String courseCode;

  @Column(name = "instructor")
  private String instructor;

  @Column(name = "instructor_contact")
  private String instructorContact;

  @Column(name = "description")
  private String description;

  @Column(name = "start_date")
  private LocalDate startDate;

  @Column(name = "end_date")
  private LocalDate endDate;

  @Column(name = "total_day_per_week")
  private Integer totalDayPerWeek;

  @Column(name = "total_hour_per_shift")
  private Integer totalHourPerShift;

  @Column(name = "created_date", nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdDate;

  @Column(name = "updated_date")
  @LastModifiedDate
  private LocalDateTime updatedDate;

  private InternshipApplication(@NonNull CreateInternshipApplicationDTO target) throws MSIException {
    getStudentService().findByUsername(target.getUsername()).ifPresent(val -> studentCode = val.getCode());
    getSemesterService().findSemesterActive().ifPresent(val -> semesterId = val.getId());
    status = InternshipApplicationStatus.NEW;
    companyId = target.getCompanyId();
    note = target.getNote();
    courseCode = target.getCourseCode();
    instructor = target.getInstructor();
    instructorContact = target.getInstructorContact();
    description = target.getDescription();
    startDate = target.getStartDate();
    endDate = target.getEndDate();
    totalDayPerWeek = target.getTotalDayPerWeek();
    totalHourPerShift = target.getTotalHourPerShift();
  }

  public static InternshipApplication getInstance(@NonNull CreateInternshipApplicationDTO dto) throws MSIException {
    return new InternshipApplication(dto);
  }

  public void update(@NonNull UpdateInternshipApplicationDTO target) {
    companyId = target.getCompanyId();
    courseCode = target.getCourseCode();
    instructor = target.getInstructor();
    instructorContact = target.getInstructorContact();
    description = target.getDescription();
    startDate = target.getStartDate();
    endDate = target.getEndDate();
    totalDayPerWeek = target.getTotalDayPerWeek();
    totalHourPerShift = target.getTotalHourPerShift();
  }

  public boolean verify(@NonNull VerifyApplicationDTO target) {
    Optional.ofNullable(Strings.trimToNull(target.getNote())).ifPresent(val -> note = val);
    if (target.isAccepted() && status == WAITING) {
      status = InternshipApplicationStatus.ACCEPTED;
      return true;
    } else if (status == WAITING) status = InternshipApplicationStatus.CANCELED;
    else throw new RuntimeException("Sinh viên chưa gửi yêu cầu đăng ký thực tập.");
    return false;
  }

  public void setStatus(@NonNull InternshipApplicationStatus status) {
    this.status = status;
  }
}
