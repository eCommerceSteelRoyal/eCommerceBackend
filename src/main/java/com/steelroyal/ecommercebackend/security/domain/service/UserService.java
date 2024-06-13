package com.steelroyal.ecommercebackend.security.domain.service;

import com.steelroyal.ecommercebackend.security.domain.model.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<User> getAll(Pageable pageable);
    User getMe();
    User authenticate(User user);
    User create(User user);
    User updateMe(User user);
    Boolean deleteMe();
}
