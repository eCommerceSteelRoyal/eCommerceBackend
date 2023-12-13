package com.steelroyal.ecommercebackend.security.domain.service;

import com.steelroyal.ecommercebackend.security.domain.model.PasswordReset;

public interface PasswordResetService {
    PasswordReset createPasswordResetToken(PasswordReset passwordReset);
}
