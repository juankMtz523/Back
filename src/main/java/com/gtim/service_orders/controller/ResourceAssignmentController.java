package com.gtim.service_orders.controller;

import com.gtim.service_orders.service.ResourceAssignmentService;
import com.gtim.service_orders.dto.DashboardAssignmentDTO;
import com.gtim.service_orders.dto.ProyectoPropuestasDTO;
import com.gtim.service_orders.dto.ResourceAssignmentDTO;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/resource-assignment")
@RequiredArgsConstructor
@Tag(name = "Resources Assignment", description = "Gestión de Asignación de Colaboradores")
public class ResourceAssignmentController {
    
    private final ResourceAssignmentService resourceAssignmetService;
    
    @Operation(summary = "Obtener los proyecto activos con orden de servicio")
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @GetMapping("/dashboard")
    public ResponseEntity<List<DashboardAssignmentDTO>> getAll(
            @RequestParam("tipo") Optional<Long> tipo,
            @AuthenticationPrincipal UserDetails user
    ) {
        
        Long tipoProyectos = tipo.orElse(2L);
        
        List<DashboardAssignmentDTO> proyectos = resourceAssignmetService.getDashboard(tipoProyectos, user.getUsername());
        
    	return proyectos.isEmpty()
    	        ? ResponseEntity.noContent().build()
    	        : ResponseEntity.ok(proyectos);
    }
        
    @Operation(summary = "Obtener los proyecto activos con orden de servicio")
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @GetMapping("/proyectos")
    public ResponseEntity<List<ProyectoPropuestasDTO>> getProyectosXPropuestas(
            @RequestParam("proyecto") Optional<Long> proyecto
    ) {
        
        Long idProyectos = proyecto.orElse(1L);
        
        List<ProyectoPropuestasDTO> propuestas = resourceAssignmetService.getPropuestasXProyecto(idProyectos);
        
    	return propuestas.isEmpty()
    	        ? ResponseEntity.noContent().build()
    	        : ResponseEntity.ok(propuestas);
    } 
    
    @Operation(
            summary = "Generar Asignación",
            description = "Generar asignación de un rol en una propuesta comercial"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Asignación creada correctamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ResourceAssignmentDTO.class)
                )
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @PostMapping
    public ResourceAssignmentDTO create(
            @RequestBody @Parameter(description = "Datos del colaborador", required = true) ResourceAssignmentDTO request,
            @AuthenticationPrincipal UserDetails user
    ) {
        return resourceAssignmetService.create(request, user.getUsername());
    }
    
    @Operation(
            summary = "Actualizar colaborador",
            description = "Actualiza la información del colaborador"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Colaborador actualizado correctamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ResourceAssignmentDTO.class)
                )
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @PutMapping
    public ResourceAssignmentDTO update(
            @RequestBody @Parameter(description = "Datos del colaborador a actualizar", required = true) ResourceAssignmentDTO request,
            @AuthenticationPrincipal UserDetails user
    ) {
        return resourceAssignmetService.update(request, user.getUsername());
    }            
    
}
