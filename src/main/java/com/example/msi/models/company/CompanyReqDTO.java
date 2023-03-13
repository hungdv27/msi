package com.example.msi.models.company;

import lombok.*;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class CompanyReqDTO implements Serializable {
  private static final long serialVersionUID = 1905122041950251207L;

  private String name;

  private String phoneNumber;

  private String email;

  private String address;

  private String status;
  private Pageable pageable;
}
