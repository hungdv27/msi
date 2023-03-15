package com.example.msi.models.company;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Data
public class CompanyDTO {
  private Integer id;
  private String name;
  private String phoneNumber;
  private String email;
  private String address;
  private Boolean status;
}
