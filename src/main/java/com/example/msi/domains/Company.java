package com.example.msi.domains;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "company")
public class Company {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "name", nullable = false, length = 255, unique = true)
  private String name;

  @Column(name = "address", length = 255)
  private String address;

  @Column(name = "phone_number", length = 15)
  private String phoneNumber;

  @Column(name = "email", length = 100)
  private String email;

  @Column(name = "status", nullable = false)
  private String status;

  @Column(name = "created_date", nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdDate;

  @Column(name = "updated_date")
  @LastModifiedDate
  private LocalDateTime updatedDate;
}
