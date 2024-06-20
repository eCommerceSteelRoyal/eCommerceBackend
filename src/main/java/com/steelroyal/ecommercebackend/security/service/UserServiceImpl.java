package com.steelroyal.ecommercebackend.security.service;

import com.steelroyal.ecommercebackend.security.domain.model.User;
import com.steelroyal.ecommercebackend.security.domain.persistence.UserRepository;
import com.steelroyal.ecommercebackend.security.domain.service.EmailService;
import com.steelroyal.ecommercebackend.security.domain.service.UserService;
import com.steelroyal.ecommercebackend.security.resource.request.EmailRequest;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:email.properties")
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final Validator validator;

    @Value("${email.URL_TIENDA}")
    private String urlTienda;
    
    private final EmailService emailService; // Inyectar EmailService

    @Override
    public User authenticate(User user) {
        if (user.getEmail() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("incomplete data");
        }

        User authenticatedUser;
        try {
            authenticatedUser = (User) authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
                ).getPrincipal();
        } catch (AuthenticationException e) {
            throw new AuthenticationException("Credenciales inválidas") {};
        }

        if(authenticatedUser.getEmailVerifiedAt() == null){
            throw new AuthenticationException("Credenciales inválidas por verificacion") {};
        }
        return authenticatedUser;

    }

    @Override
    @Transactional
    public Boolean verifyEmail(User user) {

        if (user.getUniqd() == null) {
            throw new IllegalArgumentException("incomplete data");
        }

        User userVerify = userRepository.findUserByUniqd(user.getUniqd())
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (userVerify.getEmailVerifiedAt() == null) {
            if (userRepository.updateVerificationByUniqd(user.getUniqd(), LocalDateTime.now()) == 0) {
                throw new EntityNotFoundException("User not found");
            }
        }else {
            throw new DataIntegrityViolationException("User already verified");
        }
        return true;
    }

    @Override
    public Page<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    
    @Override
    public User getMe() {
        try {
            return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (AuthenticationException e) {
            throw new AuthenticationException("Not authenticated") {};
        }
    }

    @Override
    public User register(User user) {
        user.setUniqd(generateUniqueUniqd());

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new DataIntegrityViolationException("Already existing user");
        }

        User savedUser = userRepository.save(user);
        String enlace = urlTienda + "/login?code=" + user.getUniqd();
        emailService.sendEmail(new EmailRequest(user.getEmail(), "verificacion", enlace));
        return savedUser;
    }

    private String generateUniqueUniqd() {
        String uniqd;
        do {
            uniqd = UUID.randomUUID().toString();
        } while (userRepository.findUserByUniqd(uniqd).isPresent());
        return uniqd;
    }

    @Override
    @Transactional
    public User updateMe(User user) {
        
        if (user.getFirstName() == null || user.getLastName() == null) {
            throw new IllegalArgumentException("incomplete data");
        }

        String userEmail;
        try {
            userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (AuthenticationException e) {
            throw new AuthenticationException("Not authenticated") {};
        }

        if (userRepository.updateUserByEmail(userEmail, user.getFirstName(), user.getLastName()) == 0) {
            throw new EntityNotFoundException("User not found");
        }
        
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    @Override
    @Transactional
    public Boolean deleteMe() {

        String userEmail;
        try {
            userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (AuthenticationException e) {
            throw new AuthenticationException("Not authenticated") {};
        }

        if (userRepository.deleteUserByEmail(userEmail) == 0) {
            throw new EntityNotFoundException("User not found");
        }

        return true;
    }
}
