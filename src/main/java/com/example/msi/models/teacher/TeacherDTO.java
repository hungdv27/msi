package com.example.msi.models.teacher;

import com.example.msi.domains.Teacher;
import com.example.msi.domains.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class TeacherDTO {
  private int id;
  private Integer userId;
  private boolean status;
  private String fullName;
  private String email;
  private LocalDate dob;
  private String phoneNumber;

  public TeacherDTO(Teacher teacher, User user) {
    id = teacher.getId();
    userId = teacher.getUserId();
    status = teacher.isStatus();
    fullName = user.getFullName();
    email = user.getEmail();
    dob = user.getDob();
    phoneNumber = user.getPhoneNumber();
  }

  public TeacherDTO(Teacher teacher) {
    userId = teacher.getUserId();
    status = teacher.isStatus();
  }

  public static TeacherDTO getInstance(Teacher teacher, User user) {
    return new TeacherDTO(teacher, user);
  }

  public static TeacherDTO getInstance(Teacher teacher) {
    return new TeacherDTO(teacher);
  }
}
