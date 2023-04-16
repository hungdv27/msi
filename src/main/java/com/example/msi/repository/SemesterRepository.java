package com.example.msi.repository;

import com.example.msi.domains.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SemesterRepository extends JpaRepository<Semester, Integer> {
  Integer countAllByStatus(boolean status);

  Optional<Semester> findTopByStatusIs(boolean active);

  List<Semester> findAllByAcceptInternshipRegistration(boolean status);
}
