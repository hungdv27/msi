package com.example.msi.repository;

import com.example.msi.domains.Student;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface StudentRepository extends PagingAndSortingRepository<Student, Integer>, JpaSpecificationExecutor<Student> {
  Optional<Student> findTopByUserId(int userId);
  
}
