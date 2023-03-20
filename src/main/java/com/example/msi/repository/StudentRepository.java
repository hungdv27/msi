package com.example.msi.repository;

import com.example.msi.domains.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {
  Optional<Student> findTopByUserId(int userId);
}
