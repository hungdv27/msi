package com.example.msi.repository;

import com.example.msi.domains.InternshipApplication;
import com.example.msi.domains.InternshipProcess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InternshipProcessRepository extends JpaRepository<InternshipProcess, Integer> {
  Optional<InternshipProcess> findTopByApplicationId(int applicationId);

  Page<InternshipProcess> findAll(Specification<InternshipProcess> specification, Pageable pageable);
}
