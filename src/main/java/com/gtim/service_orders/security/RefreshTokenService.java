
package com.gtim.service_orders.security;

import com.gtim.service_orders.security.entity.User;
import com.gtim.service_orders.security.entity.UserRefreshToken;

public interface RefreshTokenService {

    UserRefreshToken create(User user);

    void deleteByToken(String token);
}
