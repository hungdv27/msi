package com.example.msi.repository;

import com.example.msi.domains.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
  @Query("SELECT u FROM User u WHERE u.email = :email")
  User getUserByEmail(@Param("email") String email);

  Optional<User> findByEmail(String mail);
}
