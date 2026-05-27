package com.gtim.service_orders.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtim.service_orders.dto.CatRoleDTO;
import com.gtim.service_orders.exception.ApiError;
import com.gtim.service_orders.service.CatRoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(
    name = "Catálogos - Roles",
    description = "Endpoints para consultar el catálogo de roles del sistema"
)
@RestController
@RequestMapping("/api/v1/cat-roles")
@RequiredArgsConstructor
public class CatRoleController {

    private final CatRoleService catRoleService;
    
    @Operation(
        summary = "Obtener roles activos",
        description = "Devuelve la lista de roles activos disponibles en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de roles obtenida correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CatRoleDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "204",
            description = "No existen roles activos"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ApiError.class)
            )
        )
    })
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @GetMapping("/active")
    public ResponseEntity<List<CatRoleDTO>> findAllSimple() {
        return ResponseEntity.ok(catRoleService.getActiveRoles());
    }
}
