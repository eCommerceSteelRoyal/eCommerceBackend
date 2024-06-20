package com.steelroyal.ecommercebackend.shared.data;

import com.steelroyal.ecommercebackend.security.domain.model.*;
import com.steelroyal.ecommercebackend.security.domain.model.Module;
import com.steelroyal.ecommercebackend.security.domain.persistence.*;
import com.steelroyal.ecommercebackend.security.domain.service.EmailService;
import com.steelroyal.ecommercebackend.security.resource.request.EmailRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.UUID;
@Component
@RequiredArgsConstructor
@PropertySource("classpath:email.properties")
public class DataBaseLoader implements CommandLineRunner {
        @Value("${email.URL_TIENDA}")
        private String urlTienda;

        private final RoleRepository roleRepository;
        private final UserRepository userRepository;
        private final ModuleRepository moduleRepository;
        private final OperationRepository operationRepository;
        private final RoleOperationRepository roleOperationRepository;
        private final PasswordEncoder passwordEncoder;
        private final EmailService emailService;

        @Override
        public void run(String... args){
                loadRoles();
                loadAdminUser();
                loadUser();
        }

        private void loadRoles() {
                Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() ->
                        roleRepository.save(new Role(null, "ADMIN", new HashSet<>(), new HashSet<>()))
                );
                Role userRole = roleRepository.findByName("USER").orElseGet(() ->
                        roleRepository.save(new Role(null, "USER", new HashSet<>(), new HashSet<>()))
                );

                Module userModule = moduleRepository.findByName("userModule").orElseGet(() ->
                        moduleRepository.save(new Module(null, "userModule", "/api/v1/users", new HashSet<>()))
                );

                Operation getAllUsers = operationRepository.findByName("getAllUsers").orElseGet(() ->
                        operationRepository.save(new Operation(null, "getAllUsers", null, HttpMethodEnum.GET, userModule, new HashSet<>()))
                );
                Operation getMe = operationRepository.findByName("getMe").orElseGet(() ->
                        operationRepository.save(new Operation(null, "getMe", "/me", HttpMethodEnum.GET, userModule, new HashSet<>()))
                );
                Operation updateMe = operationRepository.findByName("updateMe").orElseGet(() ->
                        operationRepository.save(new Operation(null, "updateMe", "/me", HttpMethodEnum.PUT, userModule, new HashSet<>()))
                );
                Operation deleteMe = operationRepository.findByName("deleteMe").orElseGet(() ->
                        operationRepository.save(new Operation(null, "deleteMe", "/me", HttpMethodEnum.DELETE, userModule, new HashSet<>()))
                );

                roleOperationRepository.findByRoleIdAndOperationId(adminRole.getId(), getAllUsers.getId()).orElseGet(() ->
                        roleOperationRepository.save(new RoleOperation(null, adminRole, getAllUsers))
                );
                roleOperationRepository.findByRoleIdAndOperationId(adminRole.getId(), getMe.getId()).orElseGet(() ->
                        roleOperationRepository.save(new RoleOperation(null, adminRole, getMe))
                );
                roleOperationRepository.findByRoleIdAndOperationId(adminRole.getId(), updateMe.getId()).orElseGet(() ->
                        roleOperationRepository.save(new RoleOperation(null, adminRole, updateMe))
                );
                roleOperationRepository.findByRoleIdAndOperationId(adminRole.getId(), deleteMe.getId()).orElseGet(() ->
                        roleOperationRepository.save(new RoleOperation(null, adminRole, deleteMe))
                );

                roleOperationRepository.findByRoleIdAndOperationId(userRole.getId(), getMe.getId()).orElseGet(() ->
                        roleOperationRepository.save(new RoleOperation(null, userRole, getMe))
                );
                roleOperationRepository.findByRoleIdAndOperationId(userRole.getId(), updateMe.getId()).orElseGet(() ->
                        roleOperationRepository.save(new RoleOperation(null, userRole, updateMe))
                );
                roleOperationRepository.findByRoleIdAndOperationId(userRole.getId(), deleteMe.getId()).orElseGet(() ->
                        roleOperationRepository.save(new RoleOperation(null, userRole, deleteMe))
                );
        }

    private void loadAdminUser() {
        String adminFirstName = "Johan";
        String adminLastName = "Admin";
        String adminEmail = "j.huanca4141@gmail.com";
        String adminPassword = "password";

        userRepository.findByEmail(adminEmail).orElseGet(() -> {
                Role adminRole = roleRepository.findByName("ADMIN")
                        .orElseThrow(() -> new IllegalStateException("Role admin not found"));

                User adminUser = User.builder()
                        .firstName(adminFirstName)
                        .lastName(adminLastName)
                        .email(adminEmail)
                        .uniqd(generateUniqueUniqd())
                        .password(passwordEncoder.encode(adminPassword))
                        .emailVerifiedAt(null)
                        .role(adminRole)
                        .build();

                userRepository.save(adminUser);
                String enlace = urlTienda + "/login?code=" + adminUser.getUniqd();
                emailService.sendEmail(new EmailRequest(adminUser.getEmail(), "verificacion", enlace));
                return adminUser;
        });
    }

    private void loadUser() {
        String userFirstName = "Sonia";
        String userLastName = "User";
        String userEmail = "sonianina366@gmail.com";
        String userPassword = "password";

        userRepository.findByEmail(userEmail).orElseGet(() -> {
                Role userRole = roleRepository.findByName("USER")
                        .orElseThrow(() -> new IllegalStateException("Role user not found"));

                User userUser = User.builder()
                        .firstName(userFirstName)
                        .lastName(userLastName)
                        .email(userEmail)
                        .uniqd(generateUniqueUniqd())
                        .password(passwordEncoder.encode(userPassword))
                        .emailVerifiedAt(null)
                        .role(userRole)
                        .build();


                userRepository.save(userUser);
                String enlace = urlTienda + "/login?code=" + userUser.getUniqd();
                emailService.sendEmail(new EmailRequest(userUser.getEmail(), "verificacion", enlace));
                return userUser;
        });
    }
    private String generateUniqueUniqd() {
        String uniqd;
        do {
            uniqd = UUID.randomUUID().toString();
        } while (userRepository.findUserByUniqd(uniqd).isPresent());
        return uniqd;
    }
}
