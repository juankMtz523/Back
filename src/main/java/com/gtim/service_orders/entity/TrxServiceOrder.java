package com.gtim.service_orders.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "trx_service_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrxServiceOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idServiceOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commercial_proposal_id", nullable = false)
    private CommercialProposal proposal;
   
    @Column(name = "os_folio")
    private String osFolio;
    
    @Column(name = "file_name")
    private String fileName;
    
    @Column(name = "file_path")
    private String filePath;
    
    @Column(name = "comments")
    private String comments;
    
    @Column(name = "created_by")
    private String createdBy;
    
    @Column(name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime createdAt;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @Column(name = "updated_at")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime updatedAt;
    
    @Column(name = "active")
    private Boolean active;
    
}
