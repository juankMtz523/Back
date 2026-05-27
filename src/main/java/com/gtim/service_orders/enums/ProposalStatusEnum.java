package com.gtim.service_orders.enums;

import lombok.Getter;

@Getter
public enum ProposalStatusEnum {

    ACCEPTED_BY_CLIENT("Aceptado por el cliente"),
    REJECTED("Rechazado"),
    IN_REVIEW_BY_CLIENT("En revisión por cliente");

    private final String name;

    ProposalStatusEnum(String name) {
        this.name = name;
    }
}
