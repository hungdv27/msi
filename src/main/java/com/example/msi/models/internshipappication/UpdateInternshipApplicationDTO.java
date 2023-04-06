package com.example.msi.models.internshipappication;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class UpdateInternshipApplicationDTO {
  private int id;
  private Integer companyId;
  private List<MultipartFile> files;
  private String courseName;
  private String instructor;
  private String instructorContact;
  private String description;
  private LocalDate startDate;
  private LocalDate endDate;
  private Integer totalDayPerWeek;
  private Integer totalHourPerShift;
}
