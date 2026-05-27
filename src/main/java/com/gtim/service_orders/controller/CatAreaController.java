package com.gtim.service_orders.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtim.service_orders.dto.CatAreaDTO;
import com.gtim.service_orders.service.CatAreaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(
    name = "Catálogos - Áreas",
    description = "Endpoints para consultar el catálogo de áreas del sistema"
)
@RestController
@RequestMapping("/api/v1/cat-areas")
@RequiredArgsConstructor
public class CatAreaController {

    private final CatAreaService catAreaService;

    @Operation(
        summary = "Obtener áreas activas",
        description = "Devuelve una lista de áreas activas disponibles en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de áreas activas obtenida correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CatAreaDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "204",
            description = "No existen áreas activas"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor"
        )
    })
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @GetMapping("/active")
    public ResponseEntity<List<CatAreaDTO>> findAllSimple(
            @RequestParam("tipo") Optional<Long> tipo
    ) {
        
        Long tipoArea = tipo.orElse(1L);
        
    	List<CatAreaDTO> areas = catAreaService.getActiveAreas(tipoArea);
    	return areas.isEmpty()
    	        ? ResponseEntity.noContent().build()
    	        : ResponseEntity.ok(areas);
    }
}
