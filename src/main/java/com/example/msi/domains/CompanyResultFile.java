package com.example.msi.domains;

import com.example.msi.models.companyresult_file.CreateCompanyResultFileDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "company_result_file")
public class CompanyResultFile {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "company_result_id", nullable = false)
  private int companyResultId;

  @Column(name = "file_id", nullable = false)
  private int fileId;

  private CompanyResultFile(@NonNull CreateCompanyResultFileDTO target){
    companyResultId = target.getCompanyResultId();
    fileId = target.getFileId();
  }

  public static CompanyResultFile getInstance(@NonNull CreateCompanyResultFileDTO dto){
    return new CompanyResultFile(dto);
  }
}
