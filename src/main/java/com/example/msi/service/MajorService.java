package com.example.msi.service;

import com.example.msi.domains.Major;
import com.example.msi.exceptions.MSIException;
import com.example.msi.models.major.CreateMajorDTO;
import com.example.msi.models.major.UpdateMajorDTO;
import lombok.NonNull;

import java.util.List;

public interface MajorService {
  List<Major> getAllMajor() throws MSIException;

  void addMajor(CreateMajorDTO major) throws MSIException;

  void updateMajor(@NonNull UpdateMajorDTO payload) throws MSIException;

  void deleteMajor(int id) throws MSIException;
}
