package com.gtim.service_orders.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "trx_commercial_proposal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommercialProposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_request_id", nullable = false)
    private ServiceRequest serviceRequest;

    @Column(name = "proposal_folio", length = 50, unique = true)
    private String proposalFolio;

    @Column(name = "description")
    private String description;

    @Column(name = "assumptions")
    private String assumptions;

    @Column(name = "internal_comments")
    private String internalComments;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private CatProposalStatus status;

    @Column(name = "total_project_cost", precision = 14, scale = 2)
    private BigDecimal totalProjectCost;

    @Column(name = "sent_to_client_at")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime sentToClientAt;

    @Column(name = "expires_at")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime expiresAt;
    
    @Column(name = "reminder_sent_at")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime reminderSentAt;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "created_by", nullable = false, length = 50)
    private String createdBy;

    @Column(name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime createdAt;

    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @Column(name = "updated_at")
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private LocalDateTime updatedAt;

    @Column(name = "comments", length = 50)
    private String comments;    
    
    @OneToMany(mappedBy = "proposal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommercialProposalSection> sections;

    @OneToMany(mappedBy = "proposal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommercialProposalRole> roles;

    @OneToMany(mappedBy = "proposal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommercialProposalAttachment> attachments;
}
