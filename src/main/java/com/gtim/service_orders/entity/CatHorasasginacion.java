package com.gtim.service_orders.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cat_horasasginacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatHorasasginacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "porcentaje", nullable = false)
    private Long porcentaje;

    @Column(name = "tipo_asignacion", length = 20, nullable = false)
    private String tipoAsignacion;
    
    @Column(name = "valor", precision = 10, scale = 2, nullable = false)
    private BigDecimal valor;    
    
}
