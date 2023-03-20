package com.example.msi.domains;

import com.example.msi.models.company.CreateCompanyDTO;
import com.example.msi.models.company.IncomeCompanyCreateDTO;
import com.example.msi.models.company.UpdateCompanyDTO;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
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
  private Boolean status;

  @Column(name = "created_date", nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdDate;

  @Column(name = "updated_date")
  @LastModifiedDate
  private LocalDateTime updatedDate;

  public Company(@NonNull CreateCompanyDTO target) {
    this.name = target.getName();
    this.address = target.getAddress();
    this.email = target.getEmail();
    this.phoneNumber = target.getPhoneNumber();
    this.status = target.getStatus();
  }

  public Company(@NonNull IncomeCompanyCreateDTO target) {
    this.name = target.getName();
    this.address = target.getAddress();
    this.email = target.getEmail();
    this.phoneNumber = target.getPhoneNumber();
    this.status = true;
  }

  public void update(@NonNull UpdateCompanyDTO target) {
    this.name = target.getName();
    this.address = target.getAddress();
    this.email = target.getEmail();
    this.phoneNumber = target.getPhoneNumber();
    this.status = target.getStatus();
  }

  public static Company getInstance(@NonNull CreateCompanyDTO payload) {
    return new Company(payload);
  }
}
