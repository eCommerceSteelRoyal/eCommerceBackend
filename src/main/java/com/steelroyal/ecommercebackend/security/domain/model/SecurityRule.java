package com.steelroyal.ecommercebackend.security.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecurityRule {
    private Module module;
    private Operation operation;
    private Role role;

    public String getHttpMethod() {
        // Devolver el método HTTP asociado con la operación
        return operation.getHttpMethodEnum().toString();
    }

    public String getPath() {
        // Construir y devolver la ruta del endpoint
        if (operation.getPath() == null) {
            return module.getPath();
        } else {
            return module.getPath() + operation.getPath();
        }
    }

    public String getRole() {
        // Devolver el nombre del rol necesario
        return role.getName();
    }
}
