package com.gtim.service_orders.exception;

@SuppressWarnings("serial")
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
    
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format(
            "%s no encontrado con %s = %s",
            resourceName, fieldName, fieldValue
        ));
    }
}