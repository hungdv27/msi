package com.example.msi.service.impl;

import com.example.msi.domains.Semester;
import com.example.msi.exceptions.ExceptionUtils;
import com.example.msi.exceptions.MSIException;
import com.example.msi.models.semester.CreateSemesterDTO;
import com.example.msi.models.semester.UpdateSemesterDTO;
import com.example.msi.repository.SemesterRepository;
import com.example.msi.service.SemesterService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SemesterServiceImpl implements SemesterService {
  private final SemesterRepository repository;
  @Override
  public List<Semester> getAllSemester() throws MSIException {
    return repository.findAll();
  }

  @Override
  public void addSemester(CreateSemesterDTO semester) throws MSIException{
    if (validateDate(semester.getStartDate(), semester.getEndDate())){
      var entity = Semester.getInstance(semester);
      repository.save(entity);
    } else {
      throw new MSIException(
          ExceptionUtils.END_DATE_IS_NOT_BEFORE_OR_EQUAL_START_DATE,
          ExceptionUtils.messages.get(ExceptionUtils.END_DATE_IS_NOT_BEFORE_OR_EQUAL_START_DATE));
    }
  }

  @Override
  public void updateSemester(@NonNull UpdateSemesterDTO payload) throws MSIException {
    if (validateDate(payload.getStartDate(), payload.getEndDate())){
      var id=payload.getId();
      repository.findById(id).ifPresent(entity -> {
        entity.update(payload);
        repository.save(entity);
      });
    } else {
      throw new MSIException(
          ExceptionUtils.END_DATE_IS_NOT_BEFORE_OR_EQUAL_START_DATE,
          ExceptionUtils.messages.get(ExceptionUtils.END_DATE_IS_NOT_BEFORE_OR_EQUAL_START_DATE));
    }
  }

  @Override
  public void deleteSemester(int id) throws MSIException {
    repository.deleteById(id);
  }

  private boolean validateDate(String startDate, String endDate){
    var start = LocalDate.parse(startDate);
    var end = LocalDate.parse(endDate);
    return !end.isBefore(start) && !end.isEqual(start);
  }
}
