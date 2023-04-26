package com.example.msi.repository;

import com.example.msi.domains.Post;
import com.example.msi.shared.enums.PostApplyTo;
import com.example.msi.shared.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
  List<Post> findAllByApplyTo(PostApplyTo role);
}
