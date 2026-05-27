package com.gtim.service_orders.security.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthTokensResponse {

    private String accessToken;
    private String refreshToken;
}
