package com.gtim.service_orders.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtim.service_orders.dto.CatClientDTO;
import com.gtim.service_orders.exception.ApiError;
import com.gtim.service_orders.service.CatClientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(
    name = "Catálogos - Clientes",
    description = "Endpoints para consultar el catálogo de clientes"
)
@RestController
@RequestMapping("/api/v1/cat-clients")
@RequiredArgsConstructor
public class CatClienteController {

    private final CatClientService catClientService;

    @Operation(
        summary = "Obtener clientes activos",
        description = "Devuelve la lista de clientes activos del sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de clientes activos obtenida correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CatClientDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "204",
            description = "No existen clientes activos"
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
    public ResponseEntity<List<CatClientDTO>> getActiveClients() {
        return ResponseEntity.ok(catClientService.getActiveClients());
    }
}
