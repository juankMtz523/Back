package com.gtim.service_orders.security;


import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gtim.service_orders.security.entity.User;
import com.gtim.service_orders.security.entity.UserRefreshToken;
import com.gtim.service_orders.security.entity.UserRefreshTokenRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final UserRefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Override
    @Transactional
    public UserRefreshToken create(User user) {

        refreshTokenRepository.deleteByUser(user);

        String token =
                jwtService.generateRefreshToken(new UserPrincipal(user));

        UserRefreshToken entity = UserRefreshToken.builder()
                .user(user)
                .token(token)
                .expiresAt(
                        LocalDateTime.now()
                                .plusSeconds(jwtService.getRefreshExpirationSeconds())
                )
                .createdAt(LocalDateTime.now())
                .build();

        return refreshTokenRepository.save(entity);
    }

    @Override
    @Transactional
    public void deleteByToken(String token) {
        refreshTokenRepository.findByToken(token)
                .ifPresent(refreshTokenRepository::delete);
    }
}
