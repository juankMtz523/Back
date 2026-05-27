package com.gtim.service_orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
    name = "ResultadosCargaMasiva",
    description = "DTO Guardar registros correctos y erroneos de la carga masiva"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultadosCargaMasivaDTO {
    @Schema(
        description = "Listado de coordinadores correctos",
        example = "x"
    )
    List<CoordinadoresDTO> coordinadorCorrecto;

    @Schema(
        description = "Listado de coordinadores erroneos",
        example = "x"
    )
    List<CoordinadoresDTO> coordinadorErroneo;

    @Schema(
        description = "Listado de colaboradores correctos",
        example = "x"
    )
    List<ColaboradorDTO> colaboradorCorrecto;

    @Schema(
        description = "Listado de colaboradores correctos",
        example = "x"
    )
    List<ColaboradorDTO> colaboradorErroneo;
}
