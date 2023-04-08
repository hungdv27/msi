package com.example.msi.service;

import com.example.msi.domains.InternshipApplicationFile;
import com.example.msi.models.internshipapplication_file.CreateInternshipApplicationFileDTO;
import org.springframework.lang.NonNull;

import java.util.List;

public interface InternshipApplicationFileService {
  List<InternshipApplicationFile> findByInternshipApplicationId(int internshipApplicationFileId);

  void add(@NonNull CreateInternshipApplicationFileDTO dto);

  void deleteByInternshipApplicationId(int internshipApplicationId);

  void deleteByFileIds(@NonNull List<Integer> fileIds);
}
