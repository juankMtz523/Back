package com.gtim.service_orders.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(
        name = "ResourceAssignmentDTO",
        description = "DTO para generar las asignaciones a las propuestas comerciales"
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceAssignmentDTO extends BaseAuditDTO {

    private Long idResourceAssignment;
    private Long serviceOrderId;
    private Long resourceId;
    private Long roleId;
    private Long allocationPercent;
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDate startDate;
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDate endDate;
    private BigDecimal hourlyDate;
    private Long months;
    private BigDecimal totalByRole;
    private Long proposalRoleId;
    private Long totalHoras;
    private Long indiceRol;

}
