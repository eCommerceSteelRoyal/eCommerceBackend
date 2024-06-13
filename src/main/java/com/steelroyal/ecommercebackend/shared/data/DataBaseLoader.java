package com.steelroyal.ecommercebackend.shared.data;

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

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final ModuleRepository moduleRepository;
    private final OperationRepository operationRepository;
    private final RoleOperationRepository roleOperationRepository;
    private final PasswordEncoder passwordEncoder;

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

        Operation getAllUserProfiles = operationRepository.findByName("getAllUserProfiles").orElseGet(() ->
                operationRepository.save(new Operation(null, "getAllUserProfiles", null, HttpMethodEnum.GET, userModule, new HashSet<>()))
        );
        Operation getUserProfile = operationRepository.findByName("getUserProfile").orElseGet(() ->
                operationRepository.save(new Operation(null, "getUserProfile", "/me", HttpMethodEnum.GET, userModule, new HashSet<>()))
        );
        Operation updateUserProfile = operationRepository.findByName("updateUserProfile").orElseGet(() ->
                operationRepository.save(new Operation(null, "updateUserProfile", "/me", HttpMethodEnum.PUT, userModule, new HashSet<>()))
        );
        Operation deleteUserProfile = operationRepository.findByName("deleteUserProfile").orElseGet(() ->
                operationRepository.save(new Operation(null, "deleteUserProfile", "/me", HttpMethodEnum.DELETE, userModule, new HashSet<>()))
        );

        roleOperationRepository.findByRoleIdAndOperationId(adminRole.getId(), getAllUserProfiles.getId()).orElseGet(() ->
                roleOperationRepository.save(new RoleOperation(null, adminRole, getAllUserProfiles))
        );
        roleOperationRepository.findByRoleIdAndOperationId(adminRole.getId(), getUserProfile.getId()).orElseGet(() ->
                roleOperationRepository.save(new RoleOperation(null, adminRole, getUserProfile))
        );
        roleOperationRepository.findByRoleIdAndOperationId(adminRole.getId(), updateUserProfile.getId()).orElseGet(() ->
                roleOperationRepository.save(new RoleOperation(null, adminRole, updateUserProfile))
        );
        roleOperationRepository.findByRoleIdAndOperationId(adminRole.getId(), deleteUserProfile.getId()).orElseGet(() ->
                roleOperationRepository.save(new RoleOperation(null, adminRole, deleteUserProfile))
        );

        roleOperationRepository.findByRoleIdAndOperationId(userRole.getId(), getUserProfile.getId()).orElseGet(() ->
                roleOperationRepository.save(new RoleOperation(null, userRole, getUserProfile))
        );
        roleOperationRepository.findByRoleIdAndOperationId(userRole.getId(), updateUserProfile.getId()).orElseGet(() ->
                roleOperationRepository.save(new RoleOperation(null, userRole, updateUserProfile))
        );
        roleOperationRepository.findByRoleIdAndOperationId(userRole.getId(), deleteUserProfile.getId()).orElseGet(() ->
                roleOperationRepository.save(new RoleOperation(null, userRole, deleteUserProfile))
        );
    }

    private void loadAdminUser() {
        String adminFirstName = "Johan";
        String adminLastName = "Admin";
        String adminEmail = "admin@gmail.com";
        String adminPassword = "password";

        userRepository.findByEmail(adminEmail).orElseGet(() -> {
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new IllegalStateException("Role admin not found"));

            User adminUser = User.builder()
                    .firstName(adminFirstName)
                    .lastName(adminLastName)
                    .email(adminEmail)
                    .password(passwordEncoder.encode(adminPassword))
                    .role(adminRole)
                    .build();

            return userRepository.save(adminUser);
        });
    }

    private void loadUser() {
        String userFirstName = "Johan";
        String userLastName = "User";
        String userEmail = "user@gmail.com";
        String userPassword = "password";

        userRepository.findByEmail(userEmail).orElseGet(() -> {
            Role userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new IllegalStateException("Role user not found"));

            User adminUser = User.builder()
                    .firstName(userFirstName)
                    .lastName(userLastName)
                    .email(userEmail)
                    .password(passwordEncoder.encode(userPassword))
                    .role(userRole)
                    .build();

            return userRepository.save(adminUser);
        });
    }
}
