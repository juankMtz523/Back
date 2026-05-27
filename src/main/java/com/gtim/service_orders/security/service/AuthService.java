package com.gtim.service_orders.security.service;

import com.gtim.service_orders.security.dto.AuthTokensResponse;
import com.gtim.service_orders.security.entity.User;

public interface AuthService {

    void logout(String refreshToken);

    AuthTokensResponse refreshTokens(String refreshToken);

    User authenticate(String email, String password);
    
    User getUserInfo(String email);

}
