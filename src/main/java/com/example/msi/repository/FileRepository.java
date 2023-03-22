package com.example.msi.repository;

import com.example.msi.domains.FileE;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileE, Integer> {
}
