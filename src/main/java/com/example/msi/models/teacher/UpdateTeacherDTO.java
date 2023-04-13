package com.example.msi.models.teacher;

import com.example.msi.domains.Teacher;
import lombok.Data;

@Data
public class UpdateTeacherDTO {
  private boolean status;

  public UpdateTeacherDTO(Teacher teacher) {
    status = teacher.isStatus();
  }

  public static UpdateTeacherDTO getInstance(Teacher teacher) {
    return new UpdateTeacherDTO(teacher);
  }
}
