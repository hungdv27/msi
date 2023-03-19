package com.example.msi.models.company;

import com.example.msi.domains.Company;
import lombok.*;
import org.springframework.lang.NonNull;

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

  private CompanyDTO(@NonNull Company entity){
    id = entity.getId();
    name = entity.getName();
    phoneNumber = entity.getPhoneNumber();
    email = entity.getEmail();
    address = entity.getAddress();
    status = entity.getStatus();
  }

  public static CompanyDTO getInstance(@NonNull Company entity){
    return new CompanyDTO(entity);
  }
}
