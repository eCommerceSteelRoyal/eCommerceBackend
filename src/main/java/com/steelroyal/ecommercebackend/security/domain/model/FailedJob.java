package com.steelroyal.ecommercebackend.security.domain.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@With
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "failed_jobs")
public class FailedJob {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String connection;

  @NotNull
  private String queue;

  @Lob
  @NotNull
  private String payload;

  @Lob
  @NotNull
  private String exception;

  @Column(name = "failed_at")
  @NotNull
  private LocalDateTime failedAt;
}
