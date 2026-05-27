package com.gtim.service_orders.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cat_coordinator")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatCoordinator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @OneToOne
    @JoinColumn(name = "engineering_id", nullable = false)
    private CatArea engineering;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "gtim_role", length = 100)
    private String gtimRole;

    @Column(name = "manager_name", length = 150)
    private String managerName;

    @Column(name = "manager_email", length = 150)
    private String managerEmail;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;
}
