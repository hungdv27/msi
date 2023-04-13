package com.example.msi.repository;

import com.example.msi.domains.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher,Integer> {
  Optional<Teacher> findTopByUserId(int userId);

  Page<Teacher> findAll(Specification<Teacher> specification, Pageable pageable);
}
