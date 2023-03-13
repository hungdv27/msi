package com.example.msi.models.company;

import lombok.Data;

@Data
public class CreateCompanyDTO {

  private String name;

  private String address;

  private String phoneNumber;

  private String email;

  private Boolean status;
}
