package com.gtim.service_orders.dto;

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
public class CommercialProposalSectionDTO {

    private Long id;
    private String sectionType;
    private String content;
    private Boolean enabled;
}

