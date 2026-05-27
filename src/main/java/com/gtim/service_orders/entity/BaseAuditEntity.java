package com.gtim.service_orders.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseAuditEntity {

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime updatedAt;

    @Column(name = "active")
    private Boolean active = true;
}
