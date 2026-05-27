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
    description = "DTO que representa un recurso humano registrado en el sistema"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatResourceDTO {

    @Schema(
        description = "Identificador único del recurso",
        example = "25",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(
        description = "Nombre del recurso",
        example = "Juan"
    )
    private String firstName;

    @Schema(
        description = "Apellidos del recurso",
        example = "Pérez"
    )
    private String lastName;    

    @Schema(
        description = "Correo electrónico del recurso",
        example = "juan.perez@gtim.com.mx"
    )
    private String email;

    @Schema(
        description = "Teléfono de contacto del recurso",
        example = "+52 55 1234 5678"
    )
    private String phone;    

    @Schema(
        description = "Identificador de area del recurso dentro de GTIM",
        example = "1"
    )
    private Long areaId;

    @Schema(
        description = "Area del recurso dentro de GTIM",
        example = "Desarrollador Backend"
    )
    private String areaName;

    @Schema(
        description = "Id del rol del recurso dentro de GTIM",
        example = "1"
    )
    private Long roleId;    
    
    @Schema(
        description = "Rol del recurso dentro de GTIM",
        example = "Desarrollador Backend"
    )
    private String roleName;

    @Schema(
        description = "Identificador del coordinador asignado",
        example = "1"
    )
    private Long coordinatorId;

    @Schema(
        description = "Nombre del coordinador asignado",
        example = "Coordinador 1"
    )
    private String coordinatorName;
    
    @Schema(
        description = "Correo electrónico del coordinador asignado",
        example = "coordinador@gtim.com.mx"
    )
    private String coordinatorEmail;

    @Schema(
        description = "Indica si el recurso se encuentra activo",
        example = "true"
    )
    private Boolean active;
    
    @Schema(
        description = "Porcentaje de asignación del colaborador",
        example = "100"
    )
    private Long pctAsignacion;    
}
