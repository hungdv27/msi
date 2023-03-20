package com.example.msi.models.company;

import com.example.msi.shared.config.annotation.ExportExcel;
import com.example.msi.domains.Company;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CompanyExportDTO {
  @ExportExcel(title = "STT")
  private Integer cardinalNumber;

  @ExportExcel(title = "Tên công ty")
  private String name;

  @ExportExcel(title = "Số điện thoại")
  private String phoneNumber;

  @ExportExcel(title = "Email")
  private String email;

  @ExportExcel(title = "Địa chỉ")
  private String address;

  @ExportExcel(title = "Trạng thái")
  private String status;

  public CompanyExportDTO(int cardinalNumber, Company dto) {
    this.cardinalNumber = cardinalNumber;
    this.name = dto.getName();
    this.phoneNumber = dto.getPhoneNumber();
    this.email = dto.getEmail();
    this.address = dto.getAddress();
    this.status = String.valueOf(dto.getStatus());
  }
}
