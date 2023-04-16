package com.example.msi.models.internshipappication;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CreateInternshipApplicationDTO {
  private Integer companyId;
  private String note;
  private String username;
  private List<MultipartFile> files;
  private String courseCode;
  private String instructor;
  private String instructorContact;
  private String description;
  private LocalDate startDate;
  private LocalDate endDate;
  private Integer totalDayPerWeek;
  private Integer totalHourPerShift;
}
