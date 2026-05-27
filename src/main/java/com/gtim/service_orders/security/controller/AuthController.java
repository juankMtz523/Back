package com.gtim.service_orders.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtim.service_orders.notification.EmailService;
import com.gtim.service_orders.security.JwtService;
import com.gtim.service_orders.security.RefreshTokenService;
import com.gtim.service_orders.security.UserPrincipal;
import com.gtim.service_orders.security.dto.AuthData;
import com.gtim.service_orders.security.dto.AuthResponse;
import com.gtim.service_orders.security.dto.AuthTokensResponse;
import com.gtim.service_orders.security.dto.LoginRequest;
import com.gtim.service_orders.security.dto.LogoutRequest;
import com.gtim.service_orders.security.dto.RefreshTokenRequest;
import com.gtim.service_orders.security.entity.User;
import com.gtim.service_orders.security.entity.UserRefreshToken;
import com.gtim.service_orders.security.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(
        name = "Autenticación",
        description = "Endpoints para login, refresh de token y logout"
)
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Operation(
            summary = "Iniciar sesión",
            description = "Autentica al usuario y devuelve un access token y refresh token"
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Autenticación exitosa",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = AuthResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Credenciales inválidas",
                content = @Content
        )
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request
    ) {

        User user = authService.authenticate(
                request.getEmail(),
                request.getPassword()
        );

        UserPrincipal principal = new UserPrincipal(user);

        String accessToken = jwtService.generateAccessToken(principal);

        UserRefreshToken refreshToken
                = refreshTokenService.create(user);

        AuthData authData = new AuthData(
                accessToken,
                "Bearer",
                jwtService.getAccessExpirationSeconds(),
                refreshToken.getToken(),
                user.getFirstName(),
                user.getLastName()
        );

        return ResponseEntity.ok(
                new AuthResponse(
                        "success",
                        "Autenticación exitosa",
                        authData
                )
        );
    }

    @Operation(
            summary = "Renovar access token",
            description = "Genera un nuevo access token usando un refresh token válido"
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Token renovado correctamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = AuthResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "401",
                description = "Refresh token inválido o expirado",
                content = @Content
        )
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @RequestBody RefreshTokenRequest request
    ) {

        User user = authService.getUserInfo(request.getEmail());
        
        AuthTokensResponse tokens
                = authService.refreshTokens(request.getRefreshToken());

        AuthData authData = new AuthData(
                tokens.getAccessToken(),
                "Bearer",
                jwtService.getAccessExpirationSeconds(),
                tokens.getRefreshToken(),
                user.getFirstName(),
                user.getLastName()
        );

        return ResponseEntity.ok(
                new AuthResponse(
                        "success",
                        "Token renovado",
                        authData
                )
        );
    }

    @Operation(
            summary = "Cerrar sesión",
            description = "Invalida el refresh token y cierra la sesión del usuario"
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Sesión cerrada correctamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = AuthResponse.class)
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Refresh token inválido",
                content = @Content
        )
    })
    @PostMapping("/logout")
    public ResponseEntity<AuthResponse> logout(
            @RequestBody LogoutRequest request
    ) {

        authService.logout(request.getRefreshToken());

        return ResponseEntity.ok(
                new AuthResponse(
                        "success",
                        "Sesión cerrada correctamente",
                        null
                )
        );
    }
}
