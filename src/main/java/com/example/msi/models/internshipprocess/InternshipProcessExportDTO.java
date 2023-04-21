package com.example.msi.models.internshipprocess;

import com.example.msi.domains.InternshipProcess;
import com.example.msi.shared.config.annotation.ExportExcel;
import lombok.*;

import static com.example.msi.shared.utils.ServiceUtils.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class InternshipProcessExportDTO {
  @ExportExcel(title = "STT")
  private Integer cardinalNumber;

  @ExportExcel(title = "Họ và tên")
  private String fullName;

  @ExportExcel(title = "Mã sinh viên")
  private String studentCode;

  @ExportExcel(title = "Ngày sinh")
  private String dob;

  @ExportExcel(title = "Lớp khóa học")
  private String gradeCode;

  @ExportExcel(title = "Học phần")
  private String courseCode;
  @ExportExcel(title = "Họ và tên giảng viên")
  private String fullNameTeacher;

  @ExportExcel(title = "Email giảng viên")
  private String emailTeacher;

  @ExportExcel(title = "Điểm đánh giá")
  private String mark;

  @ExportExcel(title = "Nhận xét")
  private String review;

  public InternshipProcessExportDTO(int cardinalNumber, InternshipProcess entity) {
    // cardinalNumber
    this.cardinalNumber = cardinalNumber;

    var appId = entity.getApplicationId();
    var internshipApplication = getInternshipApplicationService().findById(appId);
    var studentCodes = internshipApplication.getStudentCode();
    var student = getStudentService().findByCode(studentCodes).orElseThrow();
    var userStudent = getUserService().findById(student.getUserId()).orElseThrow();
    // fullName
    this.fullName = userStudent.getFullName();
    // studentCode
    this.studentCode = studentCodes;
    // dob
    this.dob = String.valueOf(userStudent.getDob());
    // gradeCode
    this.gradeCode = student.getGradeCode();
    // courseCode
    this.courseCode = internshipApplication.getCourseCode();
    // fullNameTeacher
    var teacherEntity = getTeacherService().findById(entity.getTeacherId()).orElseThrow();
    var userTeacher = getUserService().findById(teacherEntity.getUserId()).orElseThrow();
    this.fullNameTeacher = userTeacher.getFullName();
    // emailTeacher
    this.emailTeacher = userTeacher.getEmail();
    // mark
    var result = getResultService().findByProcessId(entity.getId()).orElseThrow();
    this.mark = String.valueOf(result.getMark());
    // review
    this.review = result.getReview();
  }
}
