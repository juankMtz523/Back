package com.gtim.service_orders.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "ServiceRequest", description = "Solicitud de servicio que representa el ciclo completo de un proyecto")
public class ServiceRequestDTO {

    @Schema(description = "Identificador único de la solicitud", example = "1001",accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "ID del cliente", example = "10")
    private Long clientId;

    @Schema(description = "ID del área solicitante", example = "5")
    private Long areaId;

    @Schema(description = "ID del coordinador asignado", example = "3")
    private Long coordinatorId;

    @Schema(description = "Nombre del proyecto", example = "Implementación ERP")
    private String projectName;

    @Schema(description = "Fecha tentativa de inicio", example = "2026-02-01")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDate tentativeStartDate;

    @Schema(description = "Comentarios adicionales", example = "Proyecto prioritario")
    private String comments;

    @Schema(description = "Folio interno generado por el sistema", example = "SR-2026-0001", accessMode = Schema.AccessMode.READ_ONLY)
    private String internalFolio;

    /* ========= MADURACIÓN ========= */
    @Schema(description = "Estatus de maduración", example = "1")
    private Long maturationStatusId;

    @Schema(description = "Inicio de maduración", example = "2026-02-05")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDate maturationStartDate;

    @Schema(description = "Fin de maduración", example = "2026-02-20")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDate maturationEndDate;

    /* ========= CONSTRUCCIÓN ========= */
    @Schema(description = "Estatus de construcción", example = "2")
    private Long constructionStatusId;

    @Schema(description = "Inicio de construcción", example = "2026-03-01")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDate constructionStartDate;

    @Schema(description = "Fin de construcción", example = "2026-04-15")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDate constructionEndDate;

    /* ========= ESTABILIZACIÓN ========= */
    @Schema(description = "Estatus de estabilización", example = "3")
    private Long stabilizationStatusId;

    @Schema(description = "Inicio de estabilización", example = "2026-04-20")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDate stabilizationStartDate;

    @Schema(description = "Fin de estabilización", example = "2026-05-05")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDate stabilizationEndDate;

    /* ========= GENERAL ========= */
    @Schema(description = "Estatus general del proyecto", example = "1")
    private Long generalStatusId;

    @Schema(description = "Indica si la solicitud está bloqueada", example = "false")
    private Boolean isLocked;

    @Schema(description = "Indica si la solicitud está activa", example = "true", accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean active;

    @Schema(description = "Usuario que creó la solicitud", example = "jperez", accessMode = Schema.AccessMode.READ_ONLY)
    private String createdBy;

    @Schema(description = "Fecha en la que se creo", example = "01/01/1900 00:00:00", accessMode = Schema.AccessMode.READ_ONLY)
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime createdAt;

    @Schema(description = "Usuario que actualizó la solicitud", example = "mperez", accessMode = Schema.AccessMode.READ_ONLY)
    private String updatedBy;

    @Schema(description = "Folio del proyecto que asigna el cliente", example = "T1000", accessMode = Schema.AccessMode.READ_ONLY)
    private String clienteFolio;
    
    @Schema(description = "Listado de las propuestas comerciales", example = "List<CommercialProposalListDTO>", accessMode = Schema.AccessMode.READ_ONLY)
    private List<CommercialProposalListDTO> listProposal;
}
