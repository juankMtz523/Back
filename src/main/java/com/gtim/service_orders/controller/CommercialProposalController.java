package com.gtim.service_orders.controller;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gtim.service_orders.dto.CommercialProposalCreateRequestDTO;
import com.gtim.service_orders.dto.CommercialProposalDetailDTO;
import com.gtim.service_orders.dto.CommercialProposalDuplicateDTO;
import com.gtim.service_orders.dto.CommercialProposalListDTO;
import com.gtim.service_orders.dto.CommercialProposalResponseDTO;
import com.gtim.service_orders.dto.CommercialProposalSectionDTO;
import com.gtim.service_orders.dto.CommercialProposalStatusChangeRequestDTO;
import com.gtim.service_orders.dto.CommercialProposalStatusChangeResponseDTO;
import com.gtim.service_orders.dto.CommercialProposalUpdateRequestDTO;
import com.gtim.service_orders.dto.SendMailDTO;
import com.gtim.service_orders.enums.SectionType;
import com.gtim.service_orders.exception.ApiError;
import com.gtim.service_orders.service.CommercialProposalAttachmentService;
import com.gtim.service_orders.service.CommercialProposalService;
import com.microsoft.azure.storage.StorageException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/v1/commercial-proposals")
@RequiredArgsConstructor
@Tag(name = "Commercial Proposals", description = "Gestión de propuestas comerciales")
public class CommercialProposalController {

    private final CommercialProposalService commercialProposalService;
    private final CommercialProposalAttachmentService attachmentService;

    @Operation(
            summary = "Crear propuesta comercial",
            description = "Crea una nueva propuesta comercial asociada a una solicitud de servicio"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Propuesta creada correctamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = CommercialProposalResponseDTO.class)
                )
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @PostMapping
    public CommercialProposalResponseDTO create(
            @RequestBody @Parameter(description = "Datos de la propuesta comercial", required = true) CommercialProposalCreateRequestDTO request,
            @AuthenticationPrincipal UserDetails user
    ) {
        return commercialProposalService.create(request, user.getUsername());
    }

    @Operation(
            summary = "Subir archivo a sección de propuesta",
            description = "Sube un archivo adjunto a una sección específica de una propuesta comercial"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Archivo subido correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Propuesta o sección no encontrada")
    })
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @PostMapping("/{proposalId}/sections/{sectionType}/attachment")
    public void uploadAttachment(
            @PathVariable @Parameter(description = "ID de la propuesta", required = true) Long proposalId,
            @PathVariable @Parameter(description = "Tipo de sección", required = true) SectionType sectionType,
            @RequestParam("file") @Parameter(description = "Archivo a subir", required = true) MultipartFile file,
            @AuthenticationPrincipal UserDetails user
    ) throws IOException, URISyntaxException, InvalidKeyException, StorageException {
        //attachmentService.upload(proposalId, sectionType, file, user.getUsername());
        attachmentService.uploadStorage(proposalId, sectionType, file, user.getUsername());
    }

    @Operation(
            summary = "Obtener propuestas comerciales de un proyecto",
            description = "Devuelve todas las propuestas comerciales asociadas a un proyecto con paginación"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Propuestas obtenidas correctamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = CommercialProposalListDTO.class)
                )
        ),
        @ApiResponse(responseCode = "404", description = "Proyecto no encontrado")
    })
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @GetMapping("/project/{projectId}")
    public ResponseEntity<Page<CommercialProposalListDTO>> getByProject(
            @PathVariable @Parameter(description = "ID del proyecto", required = true) Long projectId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(commercialProposalService.findByProjectId(projectId, pageable));
    }

    @Operation(
            summary = "Obtener detalle de propuesta comercial",
            description = "Devuelve el detalle completo de una propuesta comercial"
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Propuesta encontrada",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = CommercialProposalDetailDTO.class)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Propuesta no encontrada",
                content = @Content(schema = @Schema(implementation = ApiError.class))
        )
    })
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @GetMapping("/{id}")
    public ResponseEntity<CommercialProposalDetailDTO> getDetail(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(commercialProposalService.findDetailById(id));
    }

    @Operation(
            summary = "Cambiar estatus de propuesta comercial",
            description = "Permite cambiar el estatus de una propuesta comercial siguiendo el flujo permitido"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estatus actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Cambio de estatus inválido"),
        @ApiResponse(responseCode = "403", description = "Acceso no autorizado"),
        @ApiResponse(responseCode = "404", description = "Propuesta no encontrada")
    })
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<CommercialProposalStatusChangeResponseDTO> changeStatus(
            @PathVariable Long id,
            @RequestBody CommercialProposalStatusChangeRequestDTO request,
            @AuthenticationPrincipal UserDetails user
    ) {
        return ResponseEntity.ok(
                commercialProposalService.changeStatus(id, request, user.getUsername())
        );
    }

    @Operation(
            summary = "Duplicar propuesta comercial",
            description = "Prepara los datos de una propuesta comercial existente para crear un duplicado. "
            + "Genera folio provisional y renombra adjuntos según las reglas de negocio."
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Datos de duplicado obtenidos correctamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = CommercialProposalDuplicateDTO.class)
                )
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Propuesta no encontrada",
                content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "El usuario no tiene permisos para duplicar esta propuesta",
                content = @Content(schema = @Schema(implementation = ApiError.class))
        )
    })
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @GetMapping("/{id}/duplicate")
    public ResponseEntity<CommercialProposalDuplicateDTO> duplicate(
            @PathVariable
            @Parameter(description = "ID de la propuesta original", required = true) Long id,
            @AuthenticationPrincipal UserDetails user
    ) {
        CommercialProposalDuplicateDTO duplicateData = commercialProposalService.duplicate(id, user.getUsername());
        return ResponseEntity.ok(duplicateData);
    }

    //TODO: falta enviar la propuesta comercial correcta
    @Operation(
            summary = "Enviar propuesta comercial al cliente",
            description = "Envía la propuesta comercial por correo electrónico al cliente. "
            + "Solo es permitido cuando la propuesta se encuentra en estatus 'Aceptado por el cliente'. "
            + "Al enviarse, se registra la fecha de envío y expiración."
    )
    @ApiResponses({
        @ApiResponse(
                responseCode = "204",
                description = "Propuesta enviada correctamente"
        ),
        @ApiResponse(
                responseCode = "400",
                description = "La propuesta no puede enviarse en su estatus actual",
                content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Propuesta no encontrada",
                content = @Content(schema = @Schema(implementation = ApiError.class))
        ),
        @ApiResponse(
                responseCode = "403",
                description = "El usuario no tiene permisos para enviar la propuesta",
                content = @Content(schema = @Schema(implementation = ApiError.class))
        )
    })
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @PutMapping("/send")
    public ResponseEntity<Void> sendProposalToClient(
            @RequestBody @Parameter(description = "Listado de correos y ID propuesta para enviar el correo con la propuesta", required = true) SendMailDTO request,
            @AuthenticationPrincipal UserDetails user
    ) {
        commercialProposalService.sendProposalToClient(request.getIdProposal(), request.getListTo(), user.getUsername());
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Generación y Descarga de PDF",
            description = "Se genera el archivo PDF a partir de la propuesta comercial"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Archivo generado correctamente"),
        @ApiResponse(responseCode = "404", description = "descarga no encontrada")
    })    
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @GetMapping("/generatePDF/{id}")
    public ResponseEntity<Resource> generatePDF(
            @PathVariable
            @Parameter(description = "ID de la propuesta comercial", required = true) Long id,
            @AuthenticationPrincipal UserDetails user) {

        ByteArrayResource reporte = commercialProposalService.downloadPDF(id);

        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename("propuestaComercial.pdf")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);

        return ResponseEntity.ok().contentLength(reporte.contentLength())
                .contentType(MediaType.APPLICATION_PDF).headers(headers)
                .body(reporte);

    }

    @Operation(
            summary = "Actualizar propuesta comercial",
            description = "Actualiza la propuesta comercial antes del estatus de aceptado por el cliente"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Propuesta actualizada correctamente",
                content = @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = CommercialProposalUpdateRequestDTO.class)
                )
        ),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @PutMapping
    public CommercialProposalResponseDTO update(
            @RequestBody @Parameter(description = "Datos de la propuesta comercial para actualizar", required = true) CommercialProposalUpdateRequestDTO request,
            @AuthenticationPrincipal UserDetails user
    ) {
        return commercialProposalService.update(request, user.getUsername());
    }    
    
    @Operation(
            summary = "Gaurdar una sección de forma individual",
            description = "Permite agregar o actualizar una sesión en especificio de la propuesta comercial"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Se guardo o actualizo correctamente"),
        @ApiResponse(responseCode = "400", description = "Sección inválida"),
        @ApiResponse(responseCode = "403", description = "Acceso no autorizado"),
        @ApiResponse(responseCode = "404", description = "Propuesta no encontrada")
    })
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @PatchMapping("/{id}/section")
    public ResponseEntity<CommercialProposalSectionDTO> addSection(
            @PathVariable Long id,
            @RequestBody CommercialProposalSectionDTO request,
            @AuthenticationPrincipal UserDetails user
    ) {
        return ResponseEntity.ok(
                commercialProposalService.addSection(id, request, user.getUsername())
        );
    }    

    @Operation(
            summary = "Subir archivo de Orden de Servicio",
            description = "Sube un archivo adjunto para la captura de la orden de servicio"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Archivo subido correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Propuesta no encontrada")
    })
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @PostMapping("/attachmentOS")
    public void uploadOrderService(
            @RequestParam("file") @Parameter(description = "Archivo a subir", required = true) MultipartFile file,
            @RequestParam("idProposal") @Parameter(description = "Id de la propuesta comercial", required = true) String idProposal,
            @RequestParam("comments") @Parameter(description = "comentarios de la orden de servicio", required = true) String comments,
            @RequestParam("idOrderService") @Parameter(description = "Identificador de la orden de servicio", required = true) String idOrderService,
            @AuthenticationPrincipal UserDetails user
    ) throws IOException, URISyntaxException, InvalidKeyException, StorageException {
        //attachmentService.upload(proposalId, sectionType, file, user.getUsername());
        attachmentService.uploadOS(Long.valueOf(idProposal), Long.valueOf(idOrderService), comments, file, user.getUsername());
    }    

    @Operation(
            summary = "Actualizar la orden de servicio",
            description = "Realizar la actualización de solo del texto de orden de servicio"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Se guardo o actualizo correctamente"),
        @ApiResponse(responseCode = "400", description = "Sección inválida"),
        @ApiResponse(responseCode = "403", description = "Acceso no autorizado"),
        @ApiResponse(responseCode = "404", description = "Propuesta no encontrada")
    })
    @PreAuthorize("hasRole('AREA_COMERCIAL')")
    @PutMapping("/{id}/updateOS")
    public ResponseEntity<CommercialProposalSectionDTO> updateOrderService(
            @PathVariable Long id,
            @RequestBody CommercialProposalSectionDTO request,
            @AuthenticationPrincipal UserDetails user
    ) {
        return ResponseEntity.ok(
                commercialProposalService.updateOrderService(id, request, user.getUsername())
        );
    }        
    
}
