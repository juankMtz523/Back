package com.gtim.service_orders.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeRequest {
    private String email;
    private String oldPassword;
    private String newPassword;
    private boolean firstTime;
}
