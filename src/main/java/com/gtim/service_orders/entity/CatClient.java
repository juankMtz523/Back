package com.gtim.service_orders.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cat_client")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "short_name", length = 50)
    private String shortName;
    
    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;

    @Column(name = "active")
    private Boolean active;
}
