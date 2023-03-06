package com.example.msi.repository;

import com.example.msi.domains.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
  @Query(value = "SELECT * FROM company WHERE name LIKE %:name%", nativeQuery = true)
  List<Company> getCompanyByName(@Param("name") String name);
}
