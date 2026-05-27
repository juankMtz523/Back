package com.gtim.service_orders.dto;

import com.gtim.service_orders.entity.CatArea;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Schema(
    name = "CatRole",
    description = "DTO que representa un rol dentro del sistema"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatRoleDTO {

    @Schema(
        description = "Identificador único del rol",
        example = "5",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(
        description = "Nombre del rol",
        example = "ADMIN"
    )
    private String name;

    @Schema(
        description = "Descripción del rol",
        example = "Rol con permisos administrativos"
    )
    private String description;
    
    @Schema(
        description = "Identificar si se muestra en el listado de roles de la propuesta",
        example = "true"
    )
    private boolean rolSystem;
    
    @Schema(
        description = "Identficador de si esta activo o no activo el rol",
        example = "true"
    )
    private boolean active;
    
    @Schema(
        description = "Area a la que pertenece el rol",
        example = "1"
    )
    private CatArea areaId;
}
