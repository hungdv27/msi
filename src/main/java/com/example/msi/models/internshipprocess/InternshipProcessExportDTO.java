package com.example.msi.models.internshipprocess;

import com.example.msi.domains.*;
import com.example.msi.shared.config.annotation.ExportExcel;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
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

  @ExportExcel(title = "Điểm đánh giá giảng viên")
  private String mark;

  @ExportExcel(title = "Nhận xét giảng viên")
  private String review;

  @ExportExcel(title = "Điểm đánh giá công ty")
  private String companyGrade;

  @ExportExcel(title = "Nhận xét công ty")
  private String companyReview;

  public InternshipProcessExportDTO(int cardinalNumber, InternshipProcess entity,
                                    Map<Integer, InternshipApplication> internshipApplicationMap,
                                    Map<String, Student> studentMap,
                                    Map<Integer, User> userMap,
                                    Map<Integer, Teacher> teacherMap,
                                    Map<Integer, Result> resultMap,
                                    Map<String, CompanyResult> companyResultMap) {
    // cardinalNumber
    this.cardinalNumber = cardinalNumber;

    var appId = entity.getApplicationId();
    var internshipApplication = internshipApplicationMap.get(appId);
    var studentCodes = internshipApplication != null ? internshipApplication.getStudentCode() : StringUtils.EMPTY;
    var student = studentMap.get(studentCodes);
    var userStudent = student != null ? userMap.get(student.getUserId()) : null;
    // fullName
    this.fullName = userStudent != null ? userStudent.getFullName() : StringUtils.EMPTY;
    // studentCode
    this.studentCode = studentCodes;
    // dob
    this.dob = userStudent != null ? String.valueOf(userStudent.getDob()) : StringUtils.EMPTY;
    // gradeCode
    this.gradeCode = student != null ? student.getGradeCode() : StringUtils.EMPTY;
    // courseCode
    this.courseCode = internshipApplication != null ? internshipApplication.getCourseCode() : StringUtils.EMPTY;
    // fullNameTeacher
    var teacherEntity = teacherMap.get(entity.getTeacherId());
    var userTeacher = teacherEntity != null ? userMap.get(teacherEntity.getUserId()) : null;
    this.fullNameTeacher = userTeacher != null ? userTeacher.getFullName() : StringUtils.EMPTY;
    // emailTeacher
    this.emailTeacher = userTeacher != null ? userTeacher.getEmail() : StringUtils.EMPTY;
    // mark
    var result = resultMap.get(entity.getId());
    this.mark = result != null ? String.valueOf(result.getMark()) : StringUtils.EMPTY;
    // review
    this.review = result != null ? result.getReview() : StringUtils.EMPTY;
    // companyGrade
    var companyResult = companyResultMap.get(studentCodes);
    this.companyGrade = companyResult != null ? String.valueOf(companyResult.getCompanyGrade()) : StringUtils.EMPTY;
    // companyReview
    this.companyReview = companyResult != null ? companyResult.getCompanyReview() : StringUtils.EMPTY;
  }
}
