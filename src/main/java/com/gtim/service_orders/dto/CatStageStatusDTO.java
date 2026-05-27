package com.gtim.service_orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Schema(
    name = "CatStageStatus",
    description = "DTO que representa el estatus de una etapa dentro del flujo del proyecto"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatStageStatusDTO {

    @Schema(
        description = "Identificador único del estatus de etapa",
        example = "3",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(
        description = "Nombre del estatus de la etapa",
        example = "EN_PROCESO"
    )
    private String name;

    @Schema(
        description = "Descripción del estatus de la etapa",
        example = "La etapa se encuentra actualmente en proceso"
    )
    private String description;
}
