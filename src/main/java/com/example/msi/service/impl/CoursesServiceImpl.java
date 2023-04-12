package com.example.msi.service.impl;

import com.example.msi.domains.Courses;
import com.example.msi.models.courses.CreateCoursesDTO;
import com.example.msi.models.courses.UpdateCoursesDTO;
import com.example.msi.repository.CoursesRepository;
import com.example.msi.service.CoursesService;
import com.example.msi.shared.exceptions.MSIException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoursesServiceImpl implements CoursesService {
  private final CoursesRepository repository;

  @Override
  public List<Courses> getAllCourses() throws MSIException {
    return repository.findAll();
  }

  @Override
  public void addCourses(@NonNull CreateCoursesDTO grade) throws MSIException {
    var entity = Courses.getInstance(grade);
    repository.save(entity);
  }

  @Override
  public void updateCourses(@NonNull UpdateCoursesDTO payload) throws MSIException {
    var id = payload.getId();
    repository.findById(id).ifPresent(entity -> {
      entity.update(payload);
      repository.save(entity);
    });
  }

  @Override
  public void deleteCourses(int id) throws MSIException {
    repository.deleteById(id);
  }
}
