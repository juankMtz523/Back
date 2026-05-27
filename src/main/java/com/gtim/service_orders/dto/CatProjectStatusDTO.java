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
    name = "CatProjectStatus",
    description = "DTO que representa un estatus de proyecto"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatProjectStatusDTO {

    @Schema(
        description = "Identificador único del estatus del proyecto",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(
        description = "Nombre del estatus del proyecto",
        example = "EN_PROGRESO"
    )
    private String name;

    @Schema(
        description = "Descripción del estatus del proyecto",
        example = "El proyecto se encuentra actualmente en ejecución"
    )
    private String description;
}
