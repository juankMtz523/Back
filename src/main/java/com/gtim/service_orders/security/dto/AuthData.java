package com.gtim.service_orders.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthData {
    private String access_token;
    private String token_type;
    private long expires_in;
    private String refresh_token;
    private String nombre;
    private String apellido;
}
