package com.gtim.service_orders.dto;

import java.util.List;
import lombok.AllArgsConstructor;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommercialProposalDuplicateDTO {

    private Long originalProposalId;
    private String originalFolio;

    private Long serviceRequestId;
    private String serviceRequestFolio;

    private String description;
    private String assumptions;
    private String internalComments;

    private List<CommercialProposalRoleRequestDTO> roles;
    private List<CommercialProposalSectionDTO> sections;
    private List<CommercialProposalAttachmentDTO> attachments;

    private String provisionalFolio;
    
    private String previewFolio;
    
    private String comments;
}
