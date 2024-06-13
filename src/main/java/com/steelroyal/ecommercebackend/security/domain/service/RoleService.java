package com.steelroyal.ecommercebackend.security.domain.service;

import com.steelroyal.ecommercebackend.security.domain.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoleService {
    Page<Role> getAll(Pageable pageable);
    Role getByName(String name);
    //Role create(Role role);
    //Role updateByName(String name, Role role);
    //Boolean deleteByName(String name);
}
