package com.example.msi.repository;

import com.example.msi.domains.InternshipApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InternshipApplicationRepository extends JpaRepository<InternshipApplication, Integer> {
  Page<InternshipApplication> findAll(Specification<InternshipApplication> specification, Pageable pageable);

  List<InternshipApplication> findAllByStudentCode(String studentCode);
}
