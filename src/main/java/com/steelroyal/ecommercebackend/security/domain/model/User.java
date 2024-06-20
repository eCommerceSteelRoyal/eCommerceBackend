package com.steelroyal.ecommercebackend.security.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Integer id;

  @NotNull
  @NotBlank
  @Column(name = "first_name", nullable = false)
  String firstName;

  @NotNull
  @NotBlank
  @Column(name = "last_name", nullable = false)
  String lastName;

  @NotNull
  @NotBlank
  @Email
  @Column(name = "email", nullable = false, unique = true)
  String email;

  @NotNull
  @NotBlank
  @Column(name = "uniqd", nullable = false, unique = true)
  String uniqd;

  @NotNull
  @NotBlank
  @Column(name = "password", nullable = false)
  String password;

  @Column(name = "email_verified_at", nullable = true)
  LocalDateTime emailVerifiedAt;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  LocalDateTime updatedAt;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "role_id", nullable = false)
  @JsonIgnore
  Role role;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_"+role.getName()));
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
