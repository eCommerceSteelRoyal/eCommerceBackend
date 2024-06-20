package com.steelroyal.ecommercebackend.security.mapping;

import com.steelroyal.ecommercebackend.security.domain.model.User;
import com.steelroyal.ecommercebackend.security.domain.service.JwtService;
import com.steelroyal.ecommercebackend.security.domain.service.RoleService;
import com.steelroyal.ecommercebackend.security.resource.request.LoginRequest;
import com.steelroyal.ecommercebackend.security.resource.request.RegisterRequest;
import com.steelroyal.ecommercebackend.security.resource.request.UniqidRequest;
import com.steelroyal.ecommercebackend.security.resource.request.UpdateUserRequest;
import com.steelroyal.ecommercebackend.security.resource.response.AuthResponse;
import com.steelroyal.ecommercebackend.security.resource.response.UserAuthResponse;
import com.steelroyal.ecommercebackend.security.resource.response.UserResponse;

import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final JwtService jwtService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse toAuthResponse(User user) {
        String token = jwtService.getToken(user);
        Date expirationTokenDate = jwtService.getExpiration(token);
        LocalDateTime expirationToken = expirationTokenDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        return AuthResponse.builder()
                .accessToken(token)
                .tokenType("bearer")
                .expiresIn(expirationToken)
                .userAuthResponse(
                    UserAuthResponse.builder()
                    .email(user.getEmail())
                    .fullName(user.getFirstName()+" "+user.getLastName())
                    .build())
                .build();
    }
    
    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
    
    public User toModel(UniqidRequest request) {
        return User.builder()
                .uniqd(request.getUniqd())
                .build();
    }

    public User toModel(LoginRequest request){
        return User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

    public User toModel(RegisterRequest request){
        return User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(roleService.getByName("USER"))
                .emailVerifiedAt(null)
                .build();
    }
    
    public User toModel(UpdateUserRequest resource){
        return User.builder()
                .firstName(resource.getFirstName())
                .lastName(resource.getLastName())
                .role(roleService.getByName("USER"))
                .build();
    }

    public Page<UserResponse> toResourcePage(Page<User> usersPage) {
        return usersPage.map(this::toUserResponse);
    }
}
