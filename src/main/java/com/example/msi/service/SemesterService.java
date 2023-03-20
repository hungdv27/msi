package com.example.msi.service;

import com.example.msi.domains.Semester;
import com.example.msi.shared.exceptions.MSIException;
import com.example.msi.models.semester.CreateSemesterDTO;
import com.example.msi.models.semester.UpdateSemesterDTO;
import lombok.NonNull;

import java.util.List;

public interface SemesterService {
  List<Semester> getAllSemester() throws MSIException;

  void addSemester(CreateSemesterDTO semester) throws MSIException;

  void updateSemester(@NonNull UpdateSemesterDTO payload) throws MSIException;

  void deleteSemester(int id) throws MSIException;
  void changeStatus(int id) throws MSIException;
}
