package com.example.msi.service;

import com.example.msi.domains.Semester;
import com.example.msi.exceptions.MSIException;
import com.example.msi.models.company.CreateSemesterDTO;
import com.example.msi.models.company.UpdateSemesterDTO;
import lombok.NonNull;

import java.util.List;

public interface SemesterService {
  List<Semester> getAllSemester() throws MSIException;;

  void addSemester(CreateSemesterDTO semester) throws MSIException;

  void updateSemester(@NonNull UpdateSemesterDTO payload) throws MSIException;

  void deleteSemester(int id) throws MSIException;;
}