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
    name = "colaboradorDTO",
    description = "POJO para realizar la carga masiva de colaboradores"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ColaboradorDTO{
    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono;
    private String area;
    private String rolGtim;
    private String coodrinador;   
    private String resultado;
}
