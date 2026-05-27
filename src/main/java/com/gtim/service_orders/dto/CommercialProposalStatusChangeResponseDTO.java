package com.gtim.service_orders.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

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
    name = "CommercialProposalStatusChangeResponse",
    description = "Respuesta del cambio de estatus de una propuesta comercial"
)
public class CommercialProposalStatusChangeResponseDTO {

    @Schema(
        description = "ID de la propuesta comercial",
        example = "15"
    )
    private Long proposalId;

    @Schema(
        description = "ID del estatus anterior",
        example = "3"
    )
    private Long previousStatusId;

    @Schema(
        description = "ID del nuevo estatus",
        example = "4"
    )
    private Long newStatusId;

    @Schema(
        description = "Nombre del nuevo estatus",
        example = "En revisión por cliente"
    )
    private String newStatusName;

    @Schema(
        description = "Fecha y hora del cambio de estatus",
        example = "2026-01-14T18:30:00"
    )
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime changedAt;
}
