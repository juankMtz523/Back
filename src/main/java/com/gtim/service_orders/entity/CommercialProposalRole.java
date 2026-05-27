package com.gtim.service_orders.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "trx_commercial_proposal_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommercialProposalRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proposal_id", nullable = false)
    private CommercialProposal proposal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private CatRole role;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "hourly_rate", precision = 12, scale = 2, nullable = false)
    private BigDecimal hourlyRate;

    @Column(name = "assignment_percentage", precision = 5, scale = 2, nullable = false)
    private BigDecimal assignmentPercentage;

    @Column(name = "assignment_type", length = 20, nullable = false)
    private String assignmentType;

    @Column(name = "months", nullable = false)
    private BigDecimal months;

    @Column(name = "total_role_cost", precision = 14, scale = 2, nullable = false)
    private BigDecimal totalRoleCost;

    @Column(name = "created_by", nullable = false, length = 50)
    private String createdBy;

    @Column(name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime createdAt;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @Column(name = "updated_at")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime updatedAt;
    
    @Column(name = "type_developer", length = 50)
    private String typeDeveloper;
}
