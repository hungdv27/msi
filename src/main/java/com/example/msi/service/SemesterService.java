package com.example.msi.service;

import com.example.msi.domains.Semester;
import com.example.msi.models.company.CreateSemesterDTO;
import com.example.msi.models.company.UpdateSemesterDTO;
import lombok.NonNull;

import java.util.List;

public interface SemesterService {
  List<Semester> getAllSemester();

  void addSemester(CreateSemesterDTO semester);

  void updateSemester(@NonNull UpdateSemesterDTO payload);

  void deleteSemester(int id);
}
