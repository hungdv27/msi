package com.example.msi.domains;

import com.example.msi.models.internshipapplication_file.CreateInternshipApplicationFileDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "internship_application_file")
public class InternshipApplicationFile {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "internship_application_id", nullable = false)
  private int internshipApplicationId;

  @Column(name = "file_id", nullable = false)
  private int fileId;

  private InternshipApplicationFile(@NonNull CreateInternshipApplicationFileDTO target){
    internshipApplicationId = target.getInternshipApplicationId();
    fileId = target.getFileId();
  }

  public static InternshipApplicationFile getInstance(@NonNull CreateInternshipApplicationFileDTO dto){
    return new InternshipApplicationFile(dto);
  }
}
