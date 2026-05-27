package com.gtim.service_orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
        name = "ProyectosColaboradorDTO",
        description = "DTO para mostrar los proyectos activos por colaborador"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProyectosColaboradorDTO {

    @Schema(
            description = "Identificador del proyecto",
            example = "1"
    )
    private Long idProyecto;

    @Schema(
            description = "Nombre del proyecto",
            example = "XXXXXXXXX"
    )
    private String nombreProyecto;

    @Schema(
            description = "Folio interno del proyecto",
            example = "XXXXX-XXXXX-XXXX"
    )
    private String folioInterno;

    @Schema(
            description = "Identificador de la propuesta comercial",
            example = "1"
    )
    private Long idPropuesta;

    @Schema(
            description = "Folio interna de la propuesta comercial",
            example = "XXXXX-XXXXXX-XXXXX"
    )
    private String folioPropuesta;

    @Schema(
            description = "Identificador del Asignación",
            example = "1"
    )
    private Long idAsignacion;

    @Schema(
            description = "Fecha de cuando inicia la asginación",
            example = "XX/XX/XXXX"
    )
    private String fechaInicio;

    @Schema(
            description = "Fecha de cuando termina la asginación",
            example = "XX/XX/XXXX"
    )
    private String fechaFin;

    @Schema(
            description = "Total de horas de la asginación de la propuesta comercial",
            example = "1"
    )
    private Long totalHoras;

    @Schema(
            description = "Total de horas cumplidas de la asignación",
            example = "1"
    )
    private Long horasCumplidas;
}
