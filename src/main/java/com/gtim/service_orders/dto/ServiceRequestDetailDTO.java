package com.gtim.service_orders.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(
    name = "ServiceRequestDetail",
    description = "Detalle completo de la solicitud de servicio"
)
public class ServiceRequestDetailDTO {

    private Long id;
    private String internalFolio;
    private String projectName;
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDate tentativeStartDate;
    
    private Long clientId;
    private String clientName;

    private Long areaId;
    private String areaName;

    private Long coordinatorId;
    private String coordinatorName;

    private Long generalStatusId;
    private String generalStatusName;

    private Long maturationStatusId;
    private String maturationStatusName;
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDate maturationStartDate;
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDate maturationEndDate;

    private Long constructionStatusId;
    private String constructionStatusName;
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDate constructionStartDate;
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDate constructionEndDate;

    private Long stabilizationStatusId;
    private String stabilizationStatusName;
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDate stabilizationStartDate;
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDate stabilizationEndDate;

    private Boolean isLocked;
    private Boolean active;

    private String comments;
    private String createdBy;
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime createdAt;
    private String updatedBy;
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime updatedAt;
    private String clienteFolio;
}
