package com.example.msi.repository;

import com.example.msi.domains.InternshipProcess;
import com.example.msi.domains.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
  Optional<Teacher> findTopByUserId(int userId);

  Page<Teacher> findAll(Specification<Teacher> specification, Pageable pageable);

  @Query(nativeQuery = true, value = """
      SELECT count(ip.id)
      FROM internship_application ia
      JOIN internship_process ip ON ia.id = ip.application_id
      JOIN teacher t ON t.id = ip.teacher_id
      JOIN semester s ON ia.semester_id = s.id
      WHERE s.status = 1 AND t.id = :teacherId
      """)
  int countNumberOfManagementStudents(int teacherId);

  @Query(nativeQuery = true, value = """
        SELECT ip.*
        FROM internship_application ia\s
        JOIN internship_process ip ON ia.id = ip.application_id
        JOIN teacher t ON t.id = ip.teacher_id
        JOIN semester s ON ia.semester_id = s.id
        WHERE s.status = 1 AND t.id = :teacherId
      """)
  List<InternshipProcess> findManagementStudents(int teacherId);
}
