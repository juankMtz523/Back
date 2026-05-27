package com.gtim.service_orders.enums;

import lombok.Getter;

@Getter
public enum ProjectStatusEnum {

    ABIERTO("Aprobado");

    private final String name;

    ProjectStatusEnum(String name) {
        this.name = name;
    }
}
