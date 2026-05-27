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
public class ServiceOrderDTO extends BaseAuditDTO {

    private Long id;
    private String osFolio;
    private String fileName;
    private String filePath;
    private String comments;    
}
