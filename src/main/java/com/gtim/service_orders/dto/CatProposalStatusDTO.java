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
    name = "CatProposalStatus",
    description = "DTO que representa un estatus de propuesta"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatProposalStatusDTO {

    @Schema(
        description = "Identificador único del estatus de la propuesta",
        example = "5",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(
        description = "Nombre del estatus de la propuesta",
        example = "APROBADA"
    )
    private String name;

    @Schema(
        description = "Descripción del estatus de la propuesta",
        example = "La propuesta fue aprobada por el comité"
    )
    private String description;
}
