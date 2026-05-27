package com.gtim.service_orders.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
    name = "appResponse",
    description = "Respuesta para los procesos que son tipo de ABC"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppResponse {
    private String id;
    private String mensaje;
    private Integer estatus;    
    private String icono;
}
