package com.gtim.service_orders.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BaseAuditDTO {

    private Long id;
    private String createdBy;
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime createdAt;
    private String updatedBy;
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime updatedAt;
    private Boolean active;

}
