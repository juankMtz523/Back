package com.gtim.service_orders.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordRecoveryRequest {
    private String email;
}
