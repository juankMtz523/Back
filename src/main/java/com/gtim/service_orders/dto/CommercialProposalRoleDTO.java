package com.gtim.service_orders.dto;

import java.math.BigDecimal;
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
public class CommercialProposalRoleDTO {
    private Long id;
    private Long roleId;
    private String name;
    private String typeDeveloper;
    private Integer quantity;
    private BigDecimal months;
    private BigDecimal hourlyRate;
    private BigDecimal assignmentPercentage;
    private BigDecimal totalRoleCost;
    private String assignmentType;
}
