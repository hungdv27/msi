package com.example.msi.repository;

import com.example.msi.domains.CompanyResultFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyResultFileRepository extends JpaRepository<CompanyResultFile, Integer> {
  List<CompanyResultFile> findAllByCompanyResultId(int companyResultId);

  void deleteAllByCompanyResultId(int companyResultId);
}
