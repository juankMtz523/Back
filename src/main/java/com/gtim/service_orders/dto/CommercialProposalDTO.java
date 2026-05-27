package com.gtim.service_orders.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class CommercialProposalDTO extends BaseAuditDTO {

    private Long serviceRequestId;
    private Long coordinatorId;
    private String proposalFolio;
    private String description;
    private String preconditions;
    private String assumptions;
    private Long statusId;
    private String internalComments;
}
