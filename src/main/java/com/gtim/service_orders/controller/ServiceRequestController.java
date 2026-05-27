package com.gtim.service_orders.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gtim.service_orders.dto.ProjectAutocompleteDTO;
import com.gtim.service_orders.dto.ProjectDashboardDTO;
import com.gtim.service_orders.dto.ServiceRequestDTO;
import com.gtim.service_orders.dto.ServiceRequestDetailDTO;
import com.gtim.service_orders.exception.ApiError;
import com.gtim.service_orders.security.UserPrincipal;
import com.gtim.service_orders.service.ServiceRequestService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

@Tag(
        name = "Service Requests",
        description = "Gestión de solicitudes de servicio"
)
@RestController
@RequestMapping("/api/v1/service-requests")
@RequiredArgsConstructor
public class ServiceRequestController {

    private final ServiceRequestService serviceRequestService;

    @Operation(
            summary = "Crear solicitud de servicio",
            description = "Crea una nueva solicitud de servicio con información inicial del proyecto"
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Solicitud creada correctamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ServiceRequestDTO.class)
                )
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Datos inválidos",
                content = @Content(schema = @Schema(implementation = ApiError.class))
        )
    })
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @PostMapping
    public ResponseEntity<ServiceRequestDTO> create(
            @RequestBody ServiceRequestDTO dto,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (userPrincipal == null) {
            return ResponseEntity.status(401).build();
        }

        String usuario = userPrincipal.getUsername();
        return ResponseEntity.ok(serviceRequestService.create(dto, usuario));
    }

    @Operation(summary = "Actualizar solicitud de servicio")
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @PutMapping("/{id}")
    public ResponseEntity<ServiceRequestDTO> update(
            @PathVariable Long id,
            @RequestBody ServiceRequestDTO dto,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(serviceRequestService.update(id, dto, userPrincipal.getUsername()));
    }

    @Operation(summary = "Obtener solicitud por ID")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Solicitud encontrada",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = ServiceRequestDetailDTO.class)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Solicitud no encontrada",
                content = @Content(schema = @Schema(implementation = ApiError.class))
        )
    })
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @GetMapping("/{id}")
    public ResponseEntity<ServiceRequestDetailDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceRequestService.findDetailById(id));
    }

    @Operation(summary = "Obtener todas las solicitudes")
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @GetMapping
    public ResponseEntity<List<ServiceRequestDTO>> getAll(@AuthenticationPrincipal UserDetails user) {
        return ResponseEntity.ok(serviceRequestService.findAll(user.getUsername()));
    }

    @Operation(summary = "Eliminar solicitud de servicio")
    @ApiResponse(responseCode = "204", description = "Solicitud eliminada")
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        serviceRequestService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Autocompletar nombres de proyecto", description = "Devuelve una lista de proyectos que coinciden parcialmente con el texto ingresado")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Resultados obtenidos correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectAutocompleteDTO.class)))})
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @GetMapping("/projects/autocomplete")
    public ResponseEntity<List<ProjectAutocompleteDTO>> autocompleteProjects(@RequestParam String query) {

        return ResponseEntity.ok(serviceRequestService.autocompleteProjects(query));
    }

    @Operation(summary = "Dashboard de proyectos", description = "Obtiene el listado paginado de proyectos para el dashboard, ordenados por fecha de creación descendente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Listado paginado de proyectos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectDashboardDTO.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado")})
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @GetMapping("/dashboard")
    public ResponseEntity<Page<ProjectDashboardDTO>> getDashboard(
            @Parameter(description = "Número de página (comienza en 0)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Cantidad de registros por página", example = "10") @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        return ResponseEntity.ok(serviceRequestService.getDashboard(pageable));
    }

}
