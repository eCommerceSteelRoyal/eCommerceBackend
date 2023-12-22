package com.steelroyal.ecommercebackend.security.service;

import com.steelroyal.ecommercebackend.security.resource.SecurityRule;
import com.steelroyal.ecommercebackend.security.domain.persistence.RoleOperationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DynamicSecurityService {

    private final RoleOperationRepository roleOperationRepository;

    public void applySecurityConfig(HttpSecurity http) throws Exception {
        List<SecurityRule> rules = roleOperationRepository.findSecurityRules();
        http.
                authorizeHttpRequests(authorizeRequests -> {
                    authorizeRequests
                            .requestMatchers("api/v1/users/auth/**").permitAll();
                    for (SecurityRule rule : rules) {
                        authorizeRequests.requestMatchers(HttpMethod.valueOf(rule.getHttpMethod()),
                                        rule.getPath())
                                .hasRole(rule.getRole());
                    }
                    authorizeRequests.anyRequest().authenticated();
                });
    }
}
