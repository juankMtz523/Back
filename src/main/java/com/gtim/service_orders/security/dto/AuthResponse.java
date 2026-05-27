package com.gtim.service_orders.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private String status;
    private String message;
    private AuthData data;
}
