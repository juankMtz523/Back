package com.gtim.service_orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Schema(description = "Solicitud de cambio de estatus de una propuesta comercial")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommercialProposalStatusChangeRequestDTO {

    @Schema(description = "ID del nuevo estatus", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long statusId;

    @Schema(
        description = "Comentario del cambio de estatus (obligatorio para rechazo)",
        example = "Propuesta enviada al cliente"
    )
    private String comment;
}
