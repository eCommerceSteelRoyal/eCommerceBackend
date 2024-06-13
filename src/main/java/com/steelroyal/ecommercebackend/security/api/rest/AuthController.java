
package com.steelroyal.ecommercebackend.security.api.rest;

import com.steelroyal.ecommercebackend.security.domain.service.UserService;
import com.steelroyal.ecommercebackend.security.mapping.UserMapper;
import com.steelroyal.ecommercebackend.security.resource.request.LoginRequest;
import com.steelroyal.ecommercebackend.security.resource.request.RegisterRequest;
import com.steelroyal.ecommercebackend.security.resource.response.AuthResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth", produces = "application/json")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(userMapper.toAuthResponse(userService.authenticate(userMapper.toModel(request))));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(userMapper.toAuthResponse(userService.create(userMapper.toModel(request))));
    }

    @GetMapping("/demo")
    public String getSuccessfully()
    {
        return "successfully GET";
    }
    @PostMapping("/demo")
    public String postSuccessfully()
    {
        return "successfully POST";
    }
}
