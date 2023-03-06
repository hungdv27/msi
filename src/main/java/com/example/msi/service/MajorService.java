package com.example.msi.service;

import com.example.msi.domains.Major;
import com.example.msi.models.company.CreateMajorDTO;
import com.example.msi.models.company.UpdateMajorDTO;
import lombok.NonNull;

import java.util.List;

public interface MajorService {
  List<Major> getAllMajor();

  void addMajor(CreateMajorDTO major);

  void updateMajor(@NonNull UpdateMajorDTO payload);

  void deleteMajor(int id);
}
