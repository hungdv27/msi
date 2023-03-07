package com.example.msi.domains;

import com.example.msi.enums.RoleEnum;
import com.example.msi.models.company.CreateCompanyDTO;
import com.example.msi.models.user.CreateUserDTO;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Table(name = "user")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "email", nullable = false, length = 100)
  private String email;

  @Column(name = "password", nullable = false, length = 250)
  private String password;

  @Column(name = "role", nullable = false)
  private RoleEnum role;

  @Column(name = "full_name", length = 255)
  private String fullName;

  @Column(name = "date_of_birth", nullable = false)
  private LocalDate dob;

  @Column(name = "phone_number", length = 15)
  private String phoneNumber;

  @Column(name = "verification_code", length = 64)
  private String verificationCode;

  @Column(name = "created_date", nullable = false, updatable = false)
  @CreatedDate
  private LocalDateTime createdDate;

  @Column(name = "updated_date")
  @LastModifiedDate
  private LocalDateTime updatedDate;

  private User(@NonNull CreateUserDTO target) {
    this.fullName = target.getFullName();
    this.password = target.getPassword();
    this.email = target.getEmail();
    this.phoneNumber = target.getPhoneNumber();
    this.dob = target.getDob();
  }

  public static User getInstance(@NonNull CreateUserDTO payload){
    return new User(payload);
  }
}
