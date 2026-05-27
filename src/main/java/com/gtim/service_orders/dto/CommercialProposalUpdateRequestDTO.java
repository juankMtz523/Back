package com.gtim.service_orders.dto;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommercialProposalUpdateRequestDTO {
    @NotNull
    private Long idProposal;
    @NotNull
    private Long serviceRequestId;
    @NotBlank
    private String description;
    private String assumptions;
    private String internalComments;
    private String provisionalFolio;
    private Long status;
    @NotEmpty
    private List<CommercialProposalSectionDTO> sections;
    @NotEmpty
    private List<CommercialProposalRoleDTO> roles;
    private List<CommercialProposalAttachmentDTO> attachments;

}
