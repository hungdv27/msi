package com.example.msi.service;

import com.example.msi.domains.Student;
import com.example.msi.models.student.UpdateStudentDTO;
import com.example.msi.shared.exceptions.MSIException;
import lombok.NonNull;

import java.util.Optional;

public interface StudentService {

  void updateStudent(@NonNull UpdateStudentDTO payload, String userName) throws MSIException;

  Optional<Student> findByUserId(String userName) throws MSIException;
}
