package com.gtim.service_orders.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtim.service_orders.dto.CatHoraAsignacionDTO;
import com.gtim.service_orders.service.CatHoraAsginacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(
    name = "Catálogos - Horas Asignación",
    description = "Endpoints para consultar el catálogo de Horas para calculo de tarifa"
)
@RestController
@RequestMapping("/api/v1/cat-hourAssigment")
@RequiredArgsConstructor
public class CatHoraAsginacionController {
    
    private final CatHoraAsginacionService service;
    
    @Operation(
        summary = "Obtener horas por asignacion",
        description = "Devuelve una lista de las horas que se obtiene por el tipo de asginación"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de áreas activas obtenida correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CatHoraAsignacionDTO.class)
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
    @GetMapping("/All")
    public ResponseEntity<List<CatHoraAsignacionDTO>> findAll() {
        
    	List<CatHoraAsignacionDTO> horas = service.getHorasAsignacion();
    	return horas.isEmpty()
    	        ? ResponseEntity.noContent().build()
    	        : ResponseEntity.ok(horas);
    }    
    
}
