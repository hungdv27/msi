package com.example.msi.service;

import com.example.msi.domains.Courses;
import com.example.msi.models.courses.CreateCoursesDTO;
import com.example.msi.models.courses.UpdateCoursesDTO;
import com.example.msi.shared.exceptions.MSIException;
import lombok.NonNull;

import java.util.List;

public interface CoursesService {
  List<Courses> getAllCourses() throws MSIException;

  void addCourses(CreateCoursesDTO grade) throws MSIException;

  void updateCourses(@NonNull UpdateCoursesDTO payload) throws MSIException;

  void deleteCourses(int id) throws MSIException;
}
