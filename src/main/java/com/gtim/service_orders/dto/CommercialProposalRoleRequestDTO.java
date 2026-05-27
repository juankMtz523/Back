package com.gtim.service_orders.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CommercialProposalRoleRequestDTO {

    @NotNull
    private Long roleId;

    @Min(1)
    private String typeDeveloper;
    
    @Min(1)
    private Integer quantity;

    @NotNull
    private BigDecimal hourlyRate;

    @NotNull
    private BigDecimal assignmentPercentage;

    @NotBlank
    private String assignmentType;

    @NotNull
    private BigDecimal months;
}

