package com.steelroyal.ecommercebackend.security.service;

import com.steelroyal.ecommercebackend.security.domain.model.Role;
import com.steelroyal.ecommercebackend.security.domain.model.User;
import com.steelroyal.ecommercebackend.security.domain.persistence.RoleRepository;
import com.steelroyal.ecommercebackend.security.domain.persistence.UserRepository;
import com.steelroyal.ecommercebackend.security.domain.service.AuthService;
import com.steelroyal.ecommercebackend.security.resource.AuthResponse;
import com.steelroyal.ecommercebackend.security.resource.LoginRequest;
import com.steelroyal.ecommercebackend.security.resource.RegisterRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    @Override
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        UserDetails user=userRepository.findByEmail(request.getEmail()).orElseThrow();
        String token=jwtService.getToken(user);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("Role user not found"));
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .rememberToken("si")
                .role(userRole)
                .build();
        userRepository.save(user);

        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();
    }
}
