package com.gtim.service_orders.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "trx_resource_assignment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrxResourceAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idResourceAssignment;

    @Column(name = "service_order_id", nullable = false)
    private Long serviceOrderId;
    
    @Column(name = "resource_id", nullable = false)
    private Long resourceId;

    @JoinColumn(name = "role_id", nullable = false)
    private Long roleId;

    @Column(name = "allocation_percent")
    private Long allocationPercent;

    @Column(name = "start_date")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDate startDate;

    @Column(name = "end_date")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDate endDate;

    @Column(name = "hourly_rate")
    private BigDecimal hourlyDate;

    @Column(name = "months")
    private Long months;

    @Column(name = "total_by_role")
    private BigDecimal totalByRole;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime updatedAt;
    
    @Column(name = "active")
    private Boolean active;
    
    @Column(name = "proporsal_role_id", nullable = false)
    private Long proposalRoleId;

    @Column(name = "indice_rol", nullable = false)
    private Long indiceRol;    
}
