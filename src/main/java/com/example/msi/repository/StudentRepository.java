package com.example.msi.repository;

import com.example.msi.domains.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
  Optional<Student> findTopByUserId(int userId);
  Optional<Student> findTopByCode(String code);
  Page<Student> findAll(Specification<Student> specification, Pageable pageable);
}
