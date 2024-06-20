package com.steelroyal.ecommercebackend.security.resource.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniqidRequest {
    private String uniqd;
}
