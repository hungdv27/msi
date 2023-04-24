package com.example.msi.repository;

import com.example.msi.domains.CompanyResultFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyResultFileRepository extends JpaRepository<CompanyResultFile, Integer> {
}
