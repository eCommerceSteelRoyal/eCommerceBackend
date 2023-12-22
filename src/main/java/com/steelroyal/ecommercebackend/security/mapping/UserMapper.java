package com.steelroyal.ecommercebackend.security.mapping;

import com.steelroyal.ecommercebackend.security.domain.model.User;
import com.steelroyal.ecommercebackend.security.resource.UserResource;

public class UserResourceMapper {
    public static UserResource fromUser(User user) {
        return UserResource.builder()
                .name(user.getName())
                .email(user.getEmail())
                .emailVerifiedAt(user.getEmailVerifiedAt()) // Asumiendo que estos campos son LocalDateTime
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
