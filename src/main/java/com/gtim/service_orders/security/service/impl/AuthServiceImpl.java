package com.gtim.service_orders.security.service.impl;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gtim.service_orders.exception.AuthenticationException;
import com.gtim.service_orders.security.JwtService;
import com.gtim.service_orders.security.RefreshTokenService;
import com.gtim.service_orders.security.UserPrincipal;
import com.gtim.service_orders.security.dto.AuthTokensResponse;
import com.gtim.service_orders.security.entity.User;
import com.gtim.service_orders.security.entity.UserRefreshToken;
import com.gtim.service_orders.security.entity.UserRefreshTokenRepository;
import com.gtim.service_orders.security.repository.UserRepository;
import com.gtim.service_orders.security.service.AuthService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    /**
     * Autentica a un usuario por correo y contraseña.
     */
    @Override
    public User authenticate(String email, String password) {

        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new AuthenticationException("Usuario o contraseña inválidos"));

        if (!user.isActive()) {
            throw new AuthenticationException("Usuario inactivo");
        }

        if (user.getLockedUntil() != null && user.getLockedUntil().isAfter(LocalDateTime.now())) {
            throw new IllegalStateException("Usuario bloqueado temporalmente hasta " + user.getLockedUntil());
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            registerFailedAttempt(user);
            throw new AuthenticationException("Usuario o contraseña inválidos");
        }

        resetAttempts(user);

        return user;
    }

    @Override
    public User getUserInfo(String email) {
        User user = userRepository.findByEmailIgnoreCase(email).get();

        return user;
    }

    private void registerFailedAttempt(User user) {
        int attempts = user.getFailedAttempts() == null ? 1 : user.getFailedAttempts() + 1;
        user.setFailedAttempts(attempts);

        if (attempts >= 5) {
            user.setLockedUntil(LocalDateTime.now().plusMinutes(15));
        }

        userRepository.save(user);
    }

    private void resetAttempts(User user) {
        user.setFailedAttempts(0);
        user.setLockedUntil(null);
        userRepository.save(user);
    }

    public String generateTempPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public void assignTempPassword(String email, String tempPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        user.setPassword(passwordEncoder.encode(tempPassword));
        user.setMustChangePassword(true);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public AuthTokensResponse refreshTokens(String refreshToken) {

        UserRefreshToken stored = refreshTokenRepository
                .findByToken(refreshToken)
                .orElseThrow(()
                        -> new AuthenticationException("Refresh token inválido")
                );

        if (stored.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(stored);
            throw new AuthenticationException("Refresh token expirado");
        }

        User user = stored.getUser();

        if (!user.isActive()) {
            throw new AuthenticationException("Usuario inactivo");
        }

        refreshTokenRepository.delete(stored);

        String newAccessToken
                = jwtService.generateAccessToken(new UserPrincipal(user));

        UserRefreshToken newRefreshToken
                = refreshTokenService.create(user);

        return new AuthTokensResponse(
                newAccessToken,
                newRefreshToken.getToken()
        );
    }

    @Override
    public void logout(String refreshToken) {

        UserRefreshToken stored = refreshTokenRepository
                .findByToken(refreshToken)
                .orElseThrow(()
                        -> new AuthenticationException("Refresh token inválido")
                );

        refreshTokenRepository.delete(stored);
    }
}
