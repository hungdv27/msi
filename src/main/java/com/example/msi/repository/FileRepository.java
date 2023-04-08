package com.example.msi.repository;

import com.example.msi.domains.FileE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface FileRepository extends JpaRepository<FileE, Integer> {
  List<FileE> findAllByIdIn(@NonNull List<Integer> ids);
  void deleteAllByIdIn(List<Integer> ids);
}
