package com.gtim.service_orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
    name = "DashboardAssignment",
    description = "DTO para mostrar los proyectos activo o inactivos para su asignación de colaboradores"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardAssignmentDTO {
    
    @Schema(
        description = "Identificador del proyecto",
        example = "1"
    )    
    private Long idProyecto;
    
    @Schema(
        description = "Nombre del proyecto a revisar",
        example = "Proyecto 1"
    )    
    private String projectName;

    @Schema(
        description = "Folio interno del proyecto para seguimiento de GTIM",
        example = "OXXO_OS_001"
    )
    private String internalFolio;

    @Schema(
        description = "Fecha de inicio del proyecto",
        example = "2026-03-01"
    )
    private String tentativeStartDate;

    @Schema(
        description = "Total de propuestas que tiene con orden de servicio el proyecto",
        example = "1"
    )
    private Long totalPropuesta;

    @Schema(
        description = "Total de roles por finalizar",
        example = "1"
    )
    private Long porFinalizar;
}
