package com.example.msi.repository;

import com.example.msi.domains.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;


@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
}
