package com.steelroyal.ecommercebackend.security.domain.service;

import com.steelroyal.ecommercebackend.security.resource.AuthResponse;
import com.steelroyal.ecommercebackend.security.resource.LoginRequest;
import com.steelroyal.ecommercebackend.security.resource.RegisterRequest;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse register(RegisterRequest request);
}
