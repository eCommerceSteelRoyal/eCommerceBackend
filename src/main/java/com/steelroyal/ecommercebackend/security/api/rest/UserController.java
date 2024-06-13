package com.steelroyal.ecommercebackend.security.api.rest;

import com.steelroyal.ecommercebackend.security.domain.service.UserService;
import com.steelroyal.ecommercebackend.security.mapping.UserMapper;
import com.steelroyal.ecommercebackend.security.resource.request.UpdateUserRequest;
import com.steelroyal.ecommercebackend.security.resource.response.UserResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/users", produces = "application/json")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userMapper.toResourcePage(userService.getAll(pageable)));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMyUser() {
        return ResponseEntity.ok(userMapper.toUserResponse(userService.getMe()));
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateMyUser(@RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userMapper.toUserResponse(userService.updateMe(userMapper.toModel(request))));
    }

    @DeleteMapping("/me")
    public ResponseEntity<Boolean> deleteMyUser() {
        return ResponseEntity.ok(userService.deleteMe());
    }
}
