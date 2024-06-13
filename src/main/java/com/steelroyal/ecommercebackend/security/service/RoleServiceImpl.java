package com.steelroyal.ecommercebackend.security.service;

import com.steelroyal.ecommercebackend.security.domain.model.Role;
import com.steelroyal.ecommercebackend.security.domain.persistence.RoleRepository;
import com.steelroyal.ecommercebackend.security.domain.service.RoleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Page<Role> getAll(Pageable pageable) {
        return roleRepository.findAll(pageable);
    }

    @Override
    public Role getByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Role with name " + name + " not found"));
    }
}
