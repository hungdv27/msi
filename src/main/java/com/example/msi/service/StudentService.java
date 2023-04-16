package com.example.msi.service;

import com.example.msi.domains.InternshipApplication;
import com.example.msi.domains.InternshipProcess;
import com.example.msi.domains.Student;
import com.example.msi.models.student.StudentDetailDTO;
import com.example.msi.models.student.UpdateStudentDTO;
import com.example.msi.shared.exceptions.MSIException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface StudentService {

  void updateStudent(@NonNull UpdateStudentDTO payload, String userName) throws MSIException;

  Optional<Student> findByUsername(String userName) throws MSIException;

  Page<StudentDetailDTO> search(String studentCode, String phone, String fullName, Pageable pageable) throws MSIException;

  Optional<Student> findByCode(@NonNull String code);

  InternshipProcess getInternshipProcess(@NonNull String username);

  List<InternshipApplication> getAllInternshipApplication(@NonNull String username);
}
