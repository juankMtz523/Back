package com.gtim.service_orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
    name = "CorreoCoordinadorDTO",
    description = "DTO para envio de proyecto nuevo"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CorreoCoordinadorDTO {
    @Schema(
        description = "Nombre del coordinador",
        example = "Coordinador 1"
    )    
    private String nombreCoordinador;
    @Schema(
        description = "Correo del coordinador",
        example = "correo@gtim.mx"
    )    
    private String correoCoordinador;    
}
