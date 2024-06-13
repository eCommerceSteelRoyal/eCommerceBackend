package com.steelroyal.ecommercebackend.security.service;

import com.steelroyal.ecommercebackend.security.domain.model.User;
import com.steelroyal.ecommercebackend.security.domain.persistence.UserRepository;
import com.steelroyal.ecommercebackend.security.domain.service.UserService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final Validator validator;

    @Override
    public User authenticate(User user) {
        try {
            return (User) authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
                ).getPrincipal();
        } catch (AuthenticationException e) {
            throw new AuthenticationException("Credenciales inválidas") {};
        }
    }

    @Override
    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    
    @Override
    public User getMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException("User with Email " + authentication.getName() + " not found"));
    }

    @Override
    public User create(User user) {
        validateUser(user);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateMe(User user) {
        validateUserForUpdate(user);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (userRepository.updateUserByEmail(authentication.getName(), user.getFirstName(), user.getLastName()) == 0) {
            throw new EntityNotFoundException("User with Email " + authentication.getName() + " not found");
        }
        
        return getMe();
    }

    @Override
    @Transactional
    public Boolean deleteMe() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (userRepository.deleteUserByEmail(authentication.getName()) == 0) {
            throw new EntityNotFoundException("No user found with email: " + authentication.getName());
        }

        return true;
    }

    private void validateUser(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new DataIntegrityViolationException("Ya existe un usuario con ese email");
        }
    }
    

    private void validateUserForUpdate(User user) {
        if (user.getFirstName() == null || user.getLastName() == null) {
            throw new IllegalArgumentException("First name and last name must not be null");
        }
    }

}
