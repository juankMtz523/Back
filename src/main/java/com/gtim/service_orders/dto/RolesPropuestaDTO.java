package com.gtim.service_orders.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
    name = "RolesPropuesta",
    description = "DTO para mostrar los roles de las propuestas comerciales"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolesPropuestaDTO {

    @Schema(
        description = "Identificador del rol en la propuesta",
        example = "1"
    )        
    private Long idRolPropuesta;
    
    @Schema(
        description = "Identificador de la propuesta",
        example = "1"
    )        
    private Long idPropuesta;
    
    @Schema(
        description = "Identificador de la orden de servicio",
        example = "1"
    )        
    private Long idOrdenServicio;
    
    @Schema(
        description = "Identificador de la asignacion",
        example = "1"
    )        
    private Long idAsignacion;
    
    @Schema(
        description = "Identificador del recurso",
        example = "1"
    )        
    private Long idColaborador;

    @Schema(
        description = "Nombre del recurso",
        example = "1"
    )        
    private String nombreColaborador;
    
    @Schema(
        description = "Identificador del rol",
        example = "1"
    )        
    private Long idRol;
    
    @Schema(
        description = "Nombre del rol",
        example = "1"
    )        
    private String nombreRol;
    
    @Schema(
        description = "Anexo de tipo de desarrollador para el rol Desarrollador",
        example = "Coordinador"
    )        
    private String tipoDesarrollador;
    
    @Schema(
        description = "Cantidad de recursos",
        example = "1"
    )        
    private Long cantidad;
    
    @Schema(
        description = "Tipo de asignación del colaborador",
        example = "Horas, Días, Semanas, Meses"
    )        
    private String tipoAsingacion;
    
    @Schema(
        description = "Porcentaje de la asignación",
        example = "100"
    )        
    private Long porcentajeAsignacion;
    
    @Schema(
        description = "Tiempo por el cual estara asignado",
        example = "1"
    )        
    private Long tiempoAsignacion;
    
    @Schema(
        description = "Fecha de inicio de asginación",
        example = "XX/XX/XXXX"
    )        
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDate fechaInicioAsignacion;
    
    @Schema(
        description = "Fecha de fin de asginación",
        example = "XX/XX/XXXX"
    )        
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDate fechaFinAsignacion;
    
    @Schema(
        description = "Total de horas de asignación",
        example = "9"
    )        
    private Long totalHorasAsignacion;
    
    @Schema(
        description = "Horas que ya se cumplieron de asignación",
        example = "9"
    )        
    private Long totalHorasCumplidas;

    @Schema(
        description = "Porcentaje de asignación total del colaborador",
        example = "9"
    )        
    private Long pctAsignacionTotal;
    
    @Schema(
        description = "Bandera para saber si ya esta finalizada la asignación, segun la fecha final",
        example = "9"
    )        
    private Long finalizado;
    
    @Schema(
        description = "Indice que se crea para cuando en el rol la cantidad es mayor a 1",
        example = "1"
    )        
    private Long indiceRol;    
}
