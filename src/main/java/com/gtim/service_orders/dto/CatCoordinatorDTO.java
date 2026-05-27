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
    name = "CatCoordinator",
    description = "DTO que representa un coordinador del sistema"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatCoordinatorDTO {

    @Schema(
        description = "Identificador único del coordinador",
        example = "25",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(
        description = "Nombre completo del coordinador",
        example = "Juan Pérez"
    )
    private String name;

    @Schema(
        description = "Correo electrónico del coordinador",
        example = "juan.perez@gtim.com"
    )
    private String email;

    @Schema(
        description = "Identificador del área de ingeniería asociada",
        example = "3"
    )
    private Long engineeringId;

    @Schema(
        description = "Nombre del área de ingeniería",
        example = "Infraestructura"
    )
    private String engineeringName;

    @Schema(
        description = "Número telefónico del coordinador",
        example = "+52 55 1234 5678"
    )
    private String phone;

    @Schema(
        description = "Rol del coordinador dentro de Grupo TI",
        example = "COORDINATOR"
    )
    private String gtimRole;

    @Schema(
        description = "Nombre del jefe directo",
        example = "María López"
    )
    private String managerName;

    @Schema(
        description = "Correo electrónico del jefe directo",
        example = "maria.lopez@gtim.com"
    )
    private String managerEmail;

    @Schema(
        description = "Estatus del coordinador en el sistema",
        example = "ACTIVE"
    )
    private String status;

    @Schema(
        description = "Indica si el coordinador se encuentra activo",
        example = "true"
    )
    private Boolean active;
}
