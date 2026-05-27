package com.gtim.service_orders.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gtim.service_orders.dto.CatCoordinatorDTO;
import com.gtim.service_orders.dto.CoordinatorRequestDTO;
import com.gtim.service_orders.dto.DesactivarResourceRequestDTO;
import com.gtim.service_orders.dto.ResourceDeleteDTO;
import com.gtim.service_orders.exception.ApiError;
import com.gtim.service_orders.service.CatCoordinatorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Tag(
    name = "Catálogos - Coordinadores",
    description = "Endpoints para consultar el catálogo de coordinadores"
)
@RestController
@RequestMapping("/api/v1/cat-coordinators")
@RequiredArgsConstructor
public class CatCoordinatorController {

    private final CatCoordinatorService service;

    @Operation(
        summary = "Obtener coordinadores activos",
        description = "Devuelve la lista de coordinadores activos registrados en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de coordinadores activos obtenida correctamente",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CatCoordinatorDTO.class)
            )
        ),
        @ApiResponse(
            responseCode = "204",
            description = "No existen coordinadores activos"
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
    public ResponseEntity<List<CatCoordinatorDTO>> findAllActive() {
        return ResponseEntity.ok(service.getActiveCoordinators());
    }
    
    @Operation(
            summary = "Crear Coordinador",
            description = "Crear un nuevo coordinador"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Coordinador creado correctamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = CoordinatorRequestDTO.class)
                )
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @PostMapping
    public CoordinatorRequestDTO create(
            @RequestBody @Parameter(description = "Datos del coordinador", required = true) CoordinatorRequestDTO request,
            @AuthenticationPrincipal UserDetails user
    ) {
        return service.create(request, user.getUsername());
    }
    
    @Operation(
            summary = "Actualizar Coordinador",
            description = "Actualiza la información del Coordinador"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Coordinador actualizado correctamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = CoordinatorRequestDTO.class)
                )
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @PutMapping
    public CoordinatorRequestDTO update(
            @RequestBody @Parameter(description = "Datos del coordinador a actualizar", required = true) CoordinatorRequestDTO request,
            @AuthenticationPrincipal UserDetails user
    ) {
        return service.update(request, user.getUsername());
    }        
 
    @Operation(
            summary = "Descarga de plantilla colaboradores para carga masiva",
            description = "Permite descargar la plantilla csv para realizar una carga masiva de colaboradores"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Archivo descragado correctamente"),
        @ApiResponse(responseCode = "404", description = "descarga no encontrada")
    })    
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @GetMapping("/descargarPlantilla")
    public ResponseEntity<InputStreamResource> descargarPlantilla(@AuthenticationPrincipal UserDetails user) {

            ByteArrayInputStream fs = service.descargarPlantilla();
            InputStreamResource file = new InputStreamResource(fs);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=plantilla_coordinadores.csv")
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                    .body(file);            

    }
    
    @Operation(
            summary = "Subir archivo para carga masiva",
            description = "Sube un archivo CSV para realizar una carga masiva de registros"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Archivo subido correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
    })
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @PostMapping("/attachment")
    public void uploadAttachment(
            @RequestParam("file") @Parameter(description = "Archivo a subir", required = true) MultipartFile file,
            @AuthenticationPrincipal UserDetails user
    ) throws IOException, URISyntaxException, InvalidKeyException {
        //attachmentService.upload(proposalId, sectionType, file, user.getUsername());
        service.cargaMasivaCoordinadores(file, user.getUsername());
    }
    
    @Operation(
            summary = "Obtener datos del colaborador para mostrar pantalla de eliminación",
            description = "Datos que se ocupan para cargar la ventana de eliminación"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
    })
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @GetMapping("/datosColaborador")
    public ResponseEntity<ResourceDeleteDTO> getResourceDelete(
            @RequestParam("id") Optional<Long> id,
            @AuthenticationPrincipal UserDetails user
    ) throws IOException, URISyntaxException, InvalidKeyException {
        
        Long idResource = id.orElse(0L);
        Long idRole = 3L;
        
        ResourceDeleteDTO datos = service.getColaboradorAEliminar(idResource, idRole);

        return ResponseEntity.ok(datos);
    }    
    
    @Operation(
            summary = "Desactivar coordinador",
            description = "Proceso para desactivar un coordinador y cambios los colaboradores y asginaciones"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Cambio realizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
    })
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @PutMapping("/inactiveCoordinator")
    public ResponseEntity<List<String>> inactivarCoordinator(
            @RequestBody @Parameter(description = "Datos del colaborador a actualizar", required = true) DesactivarResourceRequestDTO request,
            @AuthenticationPrincipal UserDetails user
    ) throws IOException, URISyntaxException, InvalidKeyException {

        Long idResourceAnt = request.getIdAnterior();
        Long idResourceNew = request.getIdNuevo();
        Long idRole = request.getIdRol();
        
        List<String> cambios = service.desactivarCoordinador(idResourceAnt, idRole, idResourceNew, user.getUsername());

        return ResponseEntity.ok(cambios);
    }        
}
