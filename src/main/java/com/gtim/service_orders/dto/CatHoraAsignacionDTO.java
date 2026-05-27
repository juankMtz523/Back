package com.gtim.service_orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Schema(
    name = "CatHoraAsignacionDTO",
    description = "DTO que se utiliza para le calculo de tarifas"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatHoraAsignacionDTO {

    @Schema(
        description = "Identificador único de la tarifa",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @Schema(
        description = "Porcentaje que se puede asginar al rol",
        example = "100",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long porcentaje;
    
    @Schema(
        description = "Tipo de asignación para la tarifa",
        example = "Horas"
    )
    private String tipoAsignacion;
    
    @Schema(
        description = "Valor dependiendo del porcentaje",
        example = "9"
    )
    private BigDecimal valor;
    
}
