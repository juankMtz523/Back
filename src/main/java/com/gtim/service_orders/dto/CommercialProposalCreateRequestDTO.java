package com.gtim.service_orders.dto;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
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
public class CommercialProposalCreateRequestDTO {

    @NotNull
    private Long serviceRequestId;

    @NotBlank
    private String description;

    private String assumptions;

    private String internalComments;
    
    private String provisionalFolio;
    
    private Long status;

    @NotEmpty
    private List<CommercialProposalSectionRequestDTO> sections;

    @NotEmpty
    private List<CommercialProposalRoleRequestDTO> roles;
    
    private List<CommercialProposalAttachmentDTO> attachments;

}
