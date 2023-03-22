package com.example.msi.models.internshipappication;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class VerifyApplicationDTO {
  private final int id;
  private final boolean accepted;
  private final String note;
}
