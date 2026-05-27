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
        name = "coordinadoresDTO",
        description = "POJO para realizar la carga masiva de coordinadores"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoordinadoresDTO {
    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono;
    private String area;
    private String coordinador;
    private String correoCoordinador;
    private String resultado;
}
