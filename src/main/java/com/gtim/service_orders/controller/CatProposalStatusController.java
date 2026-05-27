package com.gtim.service_orders.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtim.service_orders.dto.CatProposalStatusDTO;
import com.gtim.service_orders.exception.ApiError;
import com.gtim.service_orders.service.CatProposalStatusService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(
    name = "Catálogos - Estatus de Propuesta",
    description = "Endpoints para consultar el catálogo de estatus de propuestas"
)
@RestController
@RequestMapping("/api/v1/cat-proposal-status")
@RequiredArgsConstructor
public class CatProposalStatusController {

    private final CatProposalStatusService service;

    @Operation(
        summary = "Obtener estatus de propuesta activos",
        description = "Devuelve la lista de estatus de propuesta activos registrados en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de estatus de propuesta obtenida correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CatProposalStatusDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "204",
            description = "No existen estatus de propuesta activos"
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
    public ResponseEntity<List<CatProposalStatusDTO>> getActiveProposalStatuses() {
        return ResponseEntity.ok(service.getActiveProposalStatuses());
    }
}
