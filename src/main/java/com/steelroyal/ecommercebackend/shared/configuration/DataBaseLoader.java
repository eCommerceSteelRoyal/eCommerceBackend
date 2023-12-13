package com.steelroyal.ecommercebackend.shared.configuration;

import com.steelroyal.ecommercebackend.security.domain.model.*;
import com.steelroyal.ecommercebackend.security.domain.model.Module;
import com.steelroyal.ecommercebackend.security.domain.persistence.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class DataBaseLoader implements CommandLineRunner {

    // Repositorios adicionales necesarios
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ModuleRepository moduleRepository;
    private final OperationRepository operationRepository;
    private final RoleOperationRepository roleOperationRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        loadRoles();
        loadAdminUser();
        loadUser();
        // Cargas adicionales...
    }

    private void loadRoles() {
        Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() ->
                roleRepository.save(new Role(null, "ADMIN", new HashSet<>(), new HashSet<>()))
        );
        Role userRole = roleRepository.findByName("USER").orElseGet(() ->
                roleRepository.save(new Role(null, "USER", new HashSet<>(), new HashSet<>()))
        );

        // Agregar operaciones específicas solo accesibles por admin
        Module demoModule = moduleRepository.findByName("DemoModule").orElseGet(() ->
                moduleRepository.save(new Module(null, "DemoModule", "/api/v1", new HashSet<>()))
        );
        Operation demoOperation = operationRepository.findByName("DemoOperation").orElseGet(() ->
                operationRepository.save(new Operation(null, "DemoOperation", "/demo", HttpMethodEnum.POST, demoModule, new HashSet<>()))
        );

        // Asignar operación de demo a admin
        roleOperationRepository.findByRoleIdAndOperationId(adminRole.getId(), demoOperation.getId()).orElseGet(() ->
                roleOperationRepository.save(new RoleOperation(null, adminRole, demoOperation)));
    }

    private void loadAdminUser() {
        String adminName = "Admin"; // Puedes cambiarlo según tus necesidades
        String adminEmail = "admin@gmail.com";
        String adminPassword = "password"; // Deberías usar una contraseña segura

        userRepository.findByName(adminName).orElseGet(() -> {
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new IllegalStateException("Role admin not found"));

            User adminUser = User.builder()
                    .name(adminName)
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .rememberToken("si")
                    .role(adminRole)
                    .build();

            return userRepository.save(adminUser);
        });
    }

    private void loadUser() {
        String adminName = "user"; // Puedes cambiarlo según tus necesidades
        String adminEmail = "user@gmail.com";
        String adminPassword = "password"; // Deberías usar una contraseña segura

        userRepository.findByName(adminName).orElseGet(() -> {
            Role userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new IllegalStateException("Role user not found"));

            User adminUser = User.builder()
                    .name(adminName)
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .rememberToken("si")
                    .role(userRole)
                    .build();

            return userRepository.save(adminUser);
        });
    }
}
