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
public class CommercialProposalListDTO {
	 private Long id;
	    private String proposalFolio;
	    private Long statusId;
	    private String statusName;
            private String comments;
            private boolean existeOrdenServicio;
}
