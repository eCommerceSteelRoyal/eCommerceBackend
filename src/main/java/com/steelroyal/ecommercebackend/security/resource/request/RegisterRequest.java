package com.steelroyal.ecommercebackend.security.resource.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotNull
    @NotBlank
    @JsonProperty("first_name")
    String firstName;

    @NotNull
    @NotBlank
    @JsonProperty("last_name")
    String lastName;

    @NotNull
    @NotBlank
    @Email
    String email;

    @NotNull
    @NotBlank
    String password;
}
