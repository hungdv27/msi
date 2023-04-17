package com.example.msi.repository;

import com.example.msi.domains.Result;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResultRepository extends CrudRepository<Result, Integer> {
  Optional<Result> findTopByProcessId(int processId);

  boolean existsByProcessId(int processId);
}
