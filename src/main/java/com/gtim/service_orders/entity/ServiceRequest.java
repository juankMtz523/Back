package com.gtim.service_orders.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "trx_service_request")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "client_id", nullable = false)
    private CatClient client;

    @OneToOne
    @JoinColumn(name = "area_id", nullable = false)
    private CatArea area;

    @OneToOne
    @JoinColumn(name = "coordinator_id", nullable = true)
    private CatCoordinator coordinator;

    @Column(name = "project_name", nullable = false, length = 150)
    private String projectName;

    @Column(name = "tentative_start_date")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDate tentativeStartDate;

    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    @Column(name = "internal_folio", length = 50, unique = true)
    private String internalFolio;

    @ManyToOne
    @JoinColumn(name = "maturation_status", nullable = true)
    private CatStageStatus maturationStatus;
    @Column(name = "maturation_start_date")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDate maturationStartDate;
    @Column(name = "maturation_end_date")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDate maturationEndDate;

    @ManyToOne
    @JoinColumn(name = "construction_status", nullable = true)
    private CatStageStatus constructionStatus;
    @Column(name = "construction_start_date")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDate constructionStartDate;
    @Column(name = "construction_end_date")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDate constructionEndDate;

    @ManyToOne
    @JoinColumn(name = "stabilization_status", nullable = true)
    private CatStageStatus stabilizationStatus;
    @Column(name = "stabilization_start_date")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDate stabilizationStartDate;
    @Column(name = "stabilization_end_date")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDate stabilizationEndDate;

    @OneToOne
    @JoinColumn(name = "general_status_id")
    private CatProjectStatus generalStatus;

    @Column(name = "is_locked")
    private Boolean isLocked;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime createdAt;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @Column(name = "updated_at")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime updatedAt;
    
    @Column(name = "cliente_folio")
    private String clienteFolio;
}
