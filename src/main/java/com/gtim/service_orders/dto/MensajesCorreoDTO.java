package com.gtim.service_orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
    name = "MensajesCorreoDTO",
    description = "DTO el envio de actualizaciones mediante correo"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MensajesCorreoDTO {
    @Schema(
        description = "Mensaje generado para el coordinador",
        example = "Te informamos...."
    )    
    private String mensajeCoordinador;
    @Schema(
        description = "Mensaje generado para el colaborador",
        example = "Tu asignación...."
    )    
    private String mensajeColaborador;
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
    @Schema(
        description = "Nombre del colaborador",
        example = "Colaborador 1"
    )    
    private String nombreColaborador;
    @Schema(
        description = "Correo del colaborador",
        example = "correo@gtim.mx"
    )    
    private String correoColaborador;
    @Schema(
        description = "Titulo del correo a enviar",
        example = "Término de asignación"
    )    
    private String titulo;
    
}
