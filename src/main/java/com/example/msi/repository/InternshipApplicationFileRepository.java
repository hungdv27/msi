package com.example.msi.repository;

import com.example.msi.domains.InternshipApplicationFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InternshipApplicationFileRepository extends JpaRepository<InternshipApplicationFile, Integer> {
  List<InternshipApplicationFile> findAllByInternshipApplicationId(@NonNull int internshipApplicationFileId);

  void deleteAllByInternshipApplicationId(int internshipApplicationFileId);

  void deleteAllByFileIdIn(List<Integer> fileIds);

}
