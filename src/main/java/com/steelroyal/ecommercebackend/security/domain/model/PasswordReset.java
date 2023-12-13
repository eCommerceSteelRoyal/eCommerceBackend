package com.steelroyal.ecommercebackend.security.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "password_resets")
public class PasswordReset {

  @Id
  @Email
  @NotBlank
  private String email;

  @NotBlank
  private String token;

  @Column(name = "created_at")
  @NotNull
  private LocalDateTime createdAt;
}
