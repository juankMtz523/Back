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
public class ListadoEtapasDTO {
    private String nombreEtapa;
    private String estatusEtapa;
    private String fechaInicio;
    private String fechaFin;
}
