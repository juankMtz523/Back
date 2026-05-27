package com.gtim.service_orders.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cat_stage_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatStageStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;

    @Column(name = "active")
    private Boolean active;
}
