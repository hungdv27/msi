package com.example.msi.repository;

import com.example.msi.domains.Post;
import com.example.msi.shared.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

  Page<Post> findAllByApplyTo(Role role, Pageable pageable);
}
