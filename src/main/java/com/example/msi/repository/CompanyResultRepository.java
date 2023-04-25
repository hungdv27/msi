package com.example.msi.repository;

import com.example.msi.domains.CompanyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyResultRepository extends JpaRepository<CompanyResult, Integer> {
  Optional<CompanyResult> findTopByStudentCode(String studentCode);
  boolean existsByStudentCode(String studentCode);
}
