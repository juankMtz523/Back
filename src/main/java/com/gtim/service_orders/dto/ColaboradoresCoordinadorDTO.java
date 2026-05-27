package com.gtim.service_orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
        name = "ColaboradoresCoordinadorDTO",
        description = "DTO para mostrar los colaboradores de un coordinador"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ColaboradoresCoordinadorDTO {

    @Schema(
            description = "Identificador del colaborador",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Nombres del colaborador",
            example = "XXXXXXXXX"
    )
    private String firstName;

    @Schema(
            description = "Apellidos del colaborador",
            example = "XXXXXXXXXX"
    )
    private String lastName;

    @Schema(
            description = "Correo del colaborador",
            example = "XXXXX@gtim.mx"
    )
    private String email;
}
