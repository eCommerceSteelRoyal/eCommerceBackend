package com.steelroyal.ecommercebackend.security.resource.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {

    @NotNull
    @NotBlank
    @JsonProperty("first_name")
    String firstName;

    @NotNull
    @NotBlank
    @JsonProperty("last_name")
    String lastName;
    
}
