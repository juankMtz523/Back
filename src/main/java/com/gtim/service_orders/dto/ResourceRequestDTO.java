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
    name = "CatResource",
    description = "DTO que representa un colaborador dentro del sistema"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceRequestDTO {
    
    @Schema(
        description = "Identificador único del Colaborador",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )    
    private Long id;

    @Schema(
        description = "Nombre(s) del colaborador",
        example = "Jose"
    )
    private String firstName;

    @Schema(
        description = "Apellidos del colaborador",
        example = "Perez"
    )
    private String lastName;

    @Schema(
        description = "Correo del colaborador de GTIM",
        example = "correo@gtim.mx"
    )
    private String email;

    @Schema(
        description = "Telefono (preferentemente celular) del colaborador",
        example = "8111111111"
    )
    private String phone;

    @Schema(
        description = "Area a la que pertenece el colaborador",
        example = "Calidad y Testing (7)"
    )
    private Long areaId;

    @Schema(
        description = "Rol GTIM al que pertenece el colaborador",
        example = "QA Automatizado (8)"
    )
    private Long roleId;

    @Schema(
        description = "Coordinador o Jefe directo del colaborador",
        example = "María López (2)"
    )
    private Long coordinatorId;
}
