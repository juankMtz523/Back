package com.gtim.service_orders.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
    name = "ProjectDashboard",
    description = "Información resumida de un proyecto mostrada en el dashboard"
)
public class ProjectDashboardDTO {

    @Schema(
        description = "Identificador único del proyecto",
        example = "1001"
    )
    private Long id;

    @Schema(
        description = "Folio interno generado por el sistema",
        example = "SR-2026-0001"
    )
    private String internalFolio;

    @Schema(
        description = "Nombre del proyecto",
        example = "Implementación ERP"
    )
    private String projectName;

    @Schema(
        description = "ID del estatus general del proyecto",
        example = "1"
    )
    private Long generalStatusId;

    @Schema(
        description = "Nombre del estatus general del proyecto",
        example = "En proceso"
    )
    private String generalStatusName;

    @Schema(
        description = "Fecha de creación del proyecto",
        example = "14/01/2026"
    )
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDateTime createdAt;

    @Schema(
        description = "ID del área responsable del proyecto",
        example = "5"
    )
    private Long areaId;

    @Schema(
        description = "Nombre del área responsable",
        example = "Área Comercial"
    )
    private String areaName;

    @Schema(
        description = "Último usuario que actualizó el proyecto",
        example = "jperez"
    )
    private String updatedBy;
}
