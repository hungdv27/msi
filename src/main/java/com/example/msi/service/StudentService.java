package com.example.msi.service;

import com.example.msi.domains.Student;
import com.example.msi.models.student.UpdateStudentDTO;
import com.example.msi.shared.exceptions.MSIException;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface StudentService {

  void updateStudent(@NonNull UpdateStudentDTO payload, String userName) throws MSIException;

  Optional<Student> findByUserId(String userName) throws MSIException;

  Page<Student> search(String studentCode, String phone, String fullName, Pageable pageable) throws MSIException;
}
