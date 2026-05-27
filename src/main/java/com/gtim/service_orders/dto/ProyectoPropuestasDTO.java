package com.gtim.service_orders.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.gtim.service_orders.dto.RolesPropuestaDTO;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
    name = "ProyectoPropuestas",
    description = "DTO para mostrar las propuestas por proyecto en la asignación de colaboradores"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProyectoPropuestasDTO {

    @Schema(
        description = "Identificador del proyecto",
        example = "1"
    )        
    private Long proyectoId;

    @Schema(
        description = "Identificador de propuesta",
        example = "1"
    )    
    private Long propuestaId;

    @Schema(
        description = "Identificador de orden de servicio",
        example = "1"
    )    
    private Long serviceOrderId;

    @Schema(
        description = "Nombre del proyecto",
        example = "1"
    )    
    private String nombreProyecto;

    @Schema(
        description = "Folio del cliente para identificar el proyecto",
        example = "XXXXXXX"
    )    
    private String folioCliente;

    @Schema(
        description = "Folio de seguimiento interno del proyecto",
        example = "OXXO_RHEFCS_XXX"
    )    
    private String folioInterno;

    @Schema(
        description = "Fecha de inicio del proyecto",
        example = "XX/XX/XXXX"
    )    
    private Date fechaInicioProyecto;

    @Schema(
        description = "Folio que se genera para la propuesta",
        example = "XXXXXX_PC"
    )    
    private String folioPropuesta;

    @Schema(
        description = "Folio generador de la Orden de Servicio",
        example = "XXXXX_OS"
    )    
    private String folioOrdenServicio;

    @Schema(
        description = "Fecha en la que se creo la orden de servicio",
        example = "XX/XX/XXXX"
    )    
    private Date fechaOrdenservicio;
    
    @Schema(
        description = "Listado de roles par ala propuesta",
        example = "object"
    )    
    private List<RolesPropuestaDTO> listadoRolesAsignacion;
    
}
