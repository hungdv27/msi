package com.example.msi.repository;

import com.example.msi.domains.Courses;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoursesRepository extends JpaRepository<Courses, Integer> {
}
