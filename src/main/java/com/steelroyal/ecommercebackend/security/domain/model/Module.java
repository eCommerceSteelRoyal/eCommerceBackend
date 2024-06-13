package com.steelroyal.ecommercebackend.security.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "modules")
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotNull
    @NotBlank
    @Column(name = "name", nullable = false, unique = true)
    String name;

    @NotNull
    @NotBlank
    @Column(name = "path", nullable = false)
    String path; // Ruta base del módulo, como '/api/v1'

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "module")
    Set<Operation> operations;
}
