package com.example.msi.models.company;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ErrorDTO {
  private String code;
  private String message;
}
