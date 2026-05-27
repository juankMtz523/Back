package com.gtim.service_orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
	    info = @Info(
	        title = "API Órdenes de Servicio",
	        version = "v1",
	        description = "Sistema de Administración de Órdenes – Grupo TI México"
	    )
	)
@SpringBootApplication
@EnableScheduling
public class ServiceOrdersApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceOrdersApplication.class, args);
	}

}
