package com.example.msi.models.student;

import com.example.msi.domains.Student;
import com.example.msi.service.UserService;
import com.example.msi.shared.ApplicationContextHolder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Setter
public class StudentDetailDTO {
  private int id;
  private String code;
  private String gradeCode;
  private Integer userId;
  private Integer semesterId;
  private int status;
  private String fullName;
  private String email;
  private LocalDate dob;
  private String phoneNumber;

  public StudentDetailDTO(Student student) {
    id = student.getId();
    code = student.getCode();
    gradeCode = student.getGradeCode();
    userId = student.getUserId();
    semesterId = student.getSemesterId();
    status = student.getStatus();
    var user = SingletonHelper.USER_SERVICE.findById(student.getUserId()).orElseThrow();
    fullName = user.getFullName();
    email = user.getEmail();
    dob = user.getDob();
    phoneNumber = user.getPhoneNumber();
  }

  public static StudentDetailDTO getInstance(@NonNull Student entity){
    return new StudentDetailDTO(entity);
  }
  private static class SingletonHelper {
    private static final UserService USER_SERVICE =
        ApplicationContextHolder.getBean(UserService.class);
  }
}

