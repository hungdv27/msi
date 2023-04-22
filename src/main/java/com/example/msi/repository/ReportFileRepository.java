package com.example.msi.repository;

import com.example.msi.domains.ReportFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportFileRepository extends JpaRepository<ReportFile, Integer> {
  List<ReportFile> findAllByReportId(@NonNull int reportId);
  void deleteAllByReportId(int reportId);
}
