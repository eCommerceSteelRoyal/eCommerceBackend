package com.steelroyal.ecommercebackend.security.domain.service;

import com.steelroyal.ecommercebackend.security.resource.request.EmailRequest;

public interface EmailService {
    public void sendEmail(EmailRequest email);
} 
