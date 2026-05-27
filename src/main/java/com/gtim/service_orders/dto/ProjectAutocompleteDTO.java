package com.gtim.service_orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
    name = "ProjectAutocomplete",
    description = "Resultado de autocompletado para nombres de proyecto"
)
public class ProjectAutocompleteDTO {

    @Schema(description = "ID de la solicitud de servicio", example = "1001")
    private Long id;

    @Schema(description = "Nombre del proyecto", example = "Implementación ERP")
    private String projectName;
}
