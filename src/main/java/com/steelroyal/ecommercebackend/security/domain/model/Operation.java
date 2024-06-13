package com.steelroyal.ecommercebackend.security.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "operations")
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotNull
    @NotBlank
    @Column(name = "name", nullable = false, unique = true)
    String name;

    //@NotNull
    //@NotBlank
    @Column(name = "path", nullable = true)
    String path; // Ruta base del módulo, como '/api/v1'

    @NotNull
    @Enumerated(EnumType.STRING) // Añade esta anotación para manejar el enum como String en la base de datos
    @Column(name = "http_method", nullable = false)
    HttpMethodEnum httpMethodEnum;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "module_id", nullable = false)
    @JsonIgnore
    Module module;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "operation")
    Set<RoleOperation> roleOperations;
}