package com.example.msi.models.user;

import com.example.msi.shared.ValidateError;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IncomeUserCreateDTO {
  private String name; // Tên

  private String phoneNumber; // SDT

  private String email; // Email

  private LocalDate dob; // Địa chỉ

  private String password;

  private String fileId; // File import

  private Integer numberSort; // lưu trữ để file show hiện thị như file import

  private Boolean statusImport; // Lưu trạng thái bản ghi (Có thể import:true / Dòng lỗi : false)

  private Boolean isCheckDuplicate = true; // Phục vụ check duplicate

  private String checkDuplicate; // Phục vụ check duplicate

  List<ValidateError> strError = new ArrayList<>();
}
