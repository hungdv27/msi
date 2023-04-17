package com.example.msi.service;

import com.example.msi.domains.Result;
import com.example.msi.models.result.CreateResultDTO;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface ResultService {
  Result create(@NonNull CreateResultDTO dto);

  Optional<Result> findByProcessId(int processId);
}
