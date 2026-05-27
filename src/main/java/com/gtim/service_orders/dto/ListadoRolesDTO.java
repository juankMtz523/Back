package com.gtim.service_orders.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListadoRolesDTO {
    private String rol;
    private String typeDeveloper;
    private Integer cantidad;
    private String tarifa;
    private String asignacion;
    private String tiempo;
    private String costoTotal;
}
