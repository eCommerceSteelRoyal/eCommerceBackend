package com.steelroyal.ecommercebackend.shared.configuration;

import com.steelroyal.ecommercebackend.security.domain.model.HttpMethodEnum;
import com.steelroyal.ecommercebackend.security.service.DynamicSecurityService;
import com.steelroyal.ecommercebackend.shared.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authProvider;
    private final DynamicSecurityService dynamicSecurityService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        dynamicSecurityService.applySecurityConfig(http);
        return http
                .csrf(AbstractHttpConfigurer::disable)
                //Ten en cuenta que authorizeHttpRequests funciona perfectamente
//                .authorizeHttpRequests(authRequest->
//                        authRequest
//                                .requestMatchers("/auth/**").permitAll()
//                                .requestMatchers(HttpMethod.valueOf(HttpMethodEnum.POST.toString()), "/api/v1/demo").hasRole("ADMIN")
//                                .anyRequest().authenticated()
//                )
                .sessionManagement(sessionManager->
                        sessionManager
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
