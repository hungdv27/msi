package com.example.msi.domains;

import com.example.msi.models.company.IncomeCompanyCreateDTO;
import com.example.msi.models.user.IncomeUserCreateDTO;
import com.example.msi.shared.enums.Role;
import com.example.msi.models.user.CreateUserDTO;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

@Getter
@Setter
@Entity
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@Table(name = "user")
public class User implements Principal {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "email", nullable = false, length = 100)
  private String email;

  @Column(name = "password", nullable = false, length = 250)
  private String password;

  @Column(name = "role", nullable = false)
  private Role role;

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
  
  @Column(name = "enabled")
  private boolean enabled;

  @Column(name = "update_password_token", length = 64)
  private String updatePasswordToken;

  @Column(name = "last_read_time")
  private LocalDateTime lastReadTime;

  private User(@NonNull CreateUserDTO target) {
    this.fullName = target.getFullName();
    this.password = target.getPassword();
    this.email = target.getEmail();
    this.phoneNumber = target.getPhoneNumber();
    this.dob = target.getDob();
  }

  public User(@NonNull IncomeUserCreateDTO target) {
    this.fullName = target.getName();
    this.email = target.getEmail();
    this.phoneNumber = target.getPhoneNumber();
    this.dob = target.getDob();
    this.password = generateRandomString(6);
  }

  public static User getInstance(@NonNull CreateUserDTO payload) {
    return new User(payload);
  }

  public User(String email) {
    this.email = email;
  }

  @Override
  public String getName() {
  return email;
  }

  private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

  public static String generateRandomString(int length) {
    Random random = new Random();
    StringBuilder sb = new StringBuilder(length);

    for (int i = 0; i < length; i++) {
      sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
    }

    return sb.toString();
  }
}
