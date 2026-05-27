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
    name = "CatClient",
    description = "DTO que representa un cliente del catálogo"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatClientDTO {

    @Schema(
        description = "Identificador único del cliente",
        example = "10",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(
        description = "Nombre completo del cliente",
        example = "Grupo TI México"
    )
    private String name;

    @Schema(
        description = "Nombre corto o abreviatura del cliente",
        example = "GTIM"
    )
    private String shortName;
}
