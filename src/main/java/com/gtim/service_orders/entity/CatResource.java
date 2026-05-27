package com.gtim.service_orders.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cat_resource")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 150)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 150)
    private String lastName;

    @Column(name = "email", length = 150)
    private String email;    

    @Column(name = "phone", length = 20)
    private String phone;    

    @OneToOne
    @JoinColumn(name = "area_id", nullable = false)
    private CatArea area;

    @OneToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private CatRole rol;

    @OneToOne
    @JoinColumn(name = "coordinator_id", nullable = false)
    private CatCoordinator coordinador;
    
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "created_by", length = 200)
    private String createdBy;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_by", length = 200)
    private String updatedBy;

    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;
    
}
