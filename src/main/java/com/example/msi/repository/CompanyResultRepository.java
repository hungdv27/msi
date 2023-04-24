package com.example.msi.repository;

import com.example.msi.domains.CompanyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyResultRepository extends JpaRepository<CompanyResult, Integer> {
}
