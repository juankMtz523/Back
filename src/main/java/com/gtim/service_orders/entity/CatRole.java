package com.gtim.service_orders.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cat_role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", nullable = false, length = 100)
    private String description;
    
    @Column(name = "active", nullable = false)
    private Boolean active;
    
    @Column(name = "rolsystem")
    private Boolean rolSystem;
    
    @OneToOne
    @JoinColumn(name = "area_id")
    private CatArea areaId;
}
