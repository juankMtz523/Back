package com.gtim.service_orders.dto;

import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
        name = "ResourceDelete",
        description = "DTO para desactivar un colaborador o coordinador"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceDeleteDTO {

    @Schema(
            description = "Identificador del colaborador o coordinador",
            example = "1"
    )
    private Long id;

    @Schema(
            description = "Nombre completo del colaborador o coordinador",
            example = "XXXXXX"
    )
    private String nombre;

    @Schema(
            description = "Correo del colaborador o coordinador",
            example = "xxxxx@gtim.mx"
    )
    private String correo;

    @Schema(
            description = "Identificador del area del colaborador o coordinador",
            example = "1"
    )
    private Long idArea;

    @Schema(
            description = "Nombre de area del colaborador o coordinador",
            example = "XXXXXX"
    )
    private String nombreArea;

    @Schema(
            description = "Lista de proyectos asginados del colaborador o coordinador",
            example = "XXXXXX"
    )
    private List<ProyectosColaboradorDTO> proyectosAsignados;

    @Schema(
            description = "Listado de colaboradores asignados al coordinador",
            example = "XXXXXX"
    )
    private List<ColaboradoresCoordinadorDTO> colaboradoresAsignados;
}
