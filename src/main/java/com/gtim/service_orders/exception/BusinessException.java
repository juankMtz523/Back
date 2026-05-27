package com.gtim.service_orders.exception;

import lombok.Getter;

/**
 * Excepción para reglas de negocio. Se usa cuando la solicitud es válida
 * técnicamente, pero viola una regla del dominio.
 */
@SuppressWarnings("serial")
@Getter
public class BusinessException extends RuntimeException {

    private final String code;

    public BusinessException(String message) {
        super(message);
        this.code = "BUSINESS_ERROR";
    }

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
