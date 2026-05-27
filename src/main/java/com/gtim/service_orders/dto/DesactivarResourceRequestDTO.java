package com.gtim.service_orders.dto;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
        name = "DesactivarResourceRequest",
        description = "DTO para desactivar un colaborador o coordinador"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DesactivarResourceRequestDTO {

    private Long idAnterior;
    private Long idNuevo;
    private Long idRol;

}
