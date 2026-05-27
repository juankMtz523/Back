package com.gtim.service_orders.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponse {
    private String token;
    private boolean mustChangePassword;
    private Long userId;
    private Long roleId;
    private String roleName;
}
