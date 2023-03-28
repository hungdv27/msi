package com.example.msi.repository;

import com.example.msi.domains.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostFileRepository extends JpaRepository<PostFile, Integer> {
  List<PostFile> findAllByPostId(@NonNull int postId);

  void deleteAllByPostId(int postId);
}
