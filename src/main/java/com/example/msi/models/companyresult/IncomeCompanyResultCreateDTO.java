package com.example.msi.models.companyresult;

import com.example.msi.shared.ValidateError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IncomeCompanyResultCreateDTO {
  private String studentCode; // Mã sinh viên

  private String companyGrade; // Điểm công ty

  private String companyReview; // Nhận xét công ty

  private String fileId; // File import

  private Integer numberSort; // lưu trữ để file show hiện thị như file import

  private Boolean statusImport; // Lưu trạng thái bản ghi (Có thể import:true / Dòng lỗi : false)

  private Boolean isCheckDuplicate = true; // Phục vụ check duplicate

  private String checkDuplicate; // Phục vụ check duplicate

  List<ValidateError> strError = new ArrayList<>();
}
