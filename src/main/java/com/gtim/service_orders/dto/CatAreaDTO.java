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
    name = "CatArea",
    description = "DTO que representa un área del catálogo del sistema"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatAreaDTO {

    @Schema(
        description = "Identificador único del área",
        example = "AR-001",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private String id;

    @Schema(
        description = "Nombre del área",
        example = "Soporte Técnico"
    )
    private String name;

    @Schema(
        description = "Descripción del área",
        example = "Área encargada de soporte y mantenimiento de sistemas"
    )
    private String description;
}
