package com.example.msi.service;

import com.example.msi.domains.Grade;
import com.example.msi.shared.exceptions.MSIException;
import com.example.msi.models.grade.CreateGradeDTO;
import com.example.msi.models.grade.UpdateGradeDTO;
import lombok.NonNull;

import java.util.List;

public interface GradeService {
  List<Grade> getAllGrade() throws MSIException;

  void addGrade(CreateGradeDTO grade) throws MSIException;

  void updateGrade(@NonNull UpdateGradeDTO payload) throws MSIException;

  void deleteGrade(int id) throws MSIException;
}
