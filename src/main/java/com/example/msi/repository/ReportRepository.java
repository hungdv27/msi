package com.example.msi.repository;

import com.example.msi.domains.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
  List<Report> findAllByProcessId(int processId);
}
