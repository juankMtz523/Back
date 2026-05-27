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
    description = "DTO que representa un coodrinador dentro del sistema"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoordinatorRequestDTO {
    
    @Schema(
        description = "Identificador único del coordinador",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )    
    private Long id;
    
    @Schema(
        description = "Nombre del coordinador",
        example = "Juan Pérez"
    )    
    private String name;
    
    @Schema(
        description = "correo del coordinador",
        example = "juan.perez@gtim.com"
    )    
    private String email;
    
    @Schema(
        description = "Area a la que pretenece el coordinador",
        example = "Gestión de Proyectos (9)"
    )
    private Long engineeringId;
    
    @Schema(
        description = "Telefono celular del coordinador",
        example = "5551001111"
    )    
    private String phone;
}
