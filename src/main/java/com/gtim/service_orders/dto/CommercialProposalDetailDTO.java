package com.gtim.service_orders.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
public class CommercialProposalDetailDTO {

    private Long id;
    private String proposalFolio;

    private Long serviceRequestId;
    private String serviceRequestFolio;

    private Long statusId;
    private String statusName;

    private String description;
    private String assumptions;
    private String internalComments;

    private BigDecimal totalProjectCost;
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime sentToClientAt;
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime expiresAt;
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime createdAt;
    private Boolean active;

    private List<CommercialProposalRoleDTO> roles;

    private List<CommercialProposalSectionDTO> sections;

    private List<CommercialProposalAttachmentDTO> attachments;
}
