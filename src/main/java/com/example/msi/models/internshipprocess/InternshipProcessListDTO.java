package com.example.msi.models.internshipprocess;

import com.example.msi.domains.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.Map;

import static com.example.msi.shared.utils.ServiceUtils.*;

@Getter
@Setter
public class InternshipProcessListDTO {
  private int id;
  private int applicationId;
  private String studentCode;
  private String studentFullName;
  private String teacherFullName;
  private Long currentWeek;
  private LocalDate startDate;
  private LocalDate endDate;

  private InternshipProcessListDTO(@NonNull InternshipProcess entity, Map<Integer, InternshipApplication> internshipApplicationMap,
                                   Map<String, Student> studentMap, Map<Integer, User> userMap, Map<Integer, Teacher> teacherMap) {
    var appId = entity.getApplicationId();
    var internshipApplication = internshipApplicationMap.get(appId);
    // id
    id = entity.getId();
    // applicationId
    applicationId = appId;
    // studentCode
    studentCode = internshipApplication.getStudentCode();
    // studentFullName
    var student = studentMap.get(studentCode);
    var userStudent = student != null ? userMap.get(student.getUserId()) : null;
    studentFullName = userStudent != null ? userStudent.getFullName() : StringUtils.EMPTY;
    // teacherFullName
    var teacher = teacherMap.get(entity.getTeacherId());
    var userTeacher = teacher != null ? userMap.get(teacher.getUserId()) : null;
    teacherFullName = userTeacher != null ? userTeacher.getFullName() : StringUtils.EMPTY;
    // currentWeek
    currentWeek = getInternshipProcessService().currentWeekProcess(internshipApplication);
    // startDate
    startDate = internshipApplication.getStartDate();
    // endDate
    endDate = internshipApplication.getEndDate();
  }

  public static InternshipProcessListDTO getInstance(@NonNull InternshipProcess entity, Map<Integer, InternshipApplication> internshipApplicationMap,
                                                     Map<String, Student> studentMap, Map<Integer, User> userMap,
                                                     Map<Integer, Teacher> teacherMap) {
    return new InternshipProcessListDTO(entity, internshipApplicationMap, studentMap, userMap, teacherMap);
  }
}
