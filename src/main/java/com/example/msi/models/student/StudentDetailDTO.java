package com.example.msi.models.student;

import com.example.msi.domains.Student;
import com.example.msi.domains.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class StudentDetailDTO {
  private int id;
  private String code;
  private String gradeCode;
  private Integer userId;
  private int status;
  private String fullName;
  private String email;
  private LocalDate dob;

  public StudentDetailDTO(Student student, User user) {
    this.id = student.getId();
    this.code = student.getCode();
    this.gradeCode = student.getGradeCode();
    this.userId = student.getUserId();
    this.status = student.getId();
    this.fullName = user.getFullName();
    this.email = user.getEmail();
    this.dob = user.getDob();
  }
}
