package com.gtim.service_orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gtim.service_orders.entity.CommercialProposalAttachment;
import com.gtim.service_orders.enums.SectionType;
import java.util.List;

public interface CommercialProposalAttachmentRepository
        extends JpaRepository<CommercialProposalAttachment, Long> {

    boolean existsByProposalIdAndSectionType(Long proposalId, SectionType sectionType);

    CommercialProposalAttachment findByProposalIdAndSectionType(Long proposalId, SectionType sectionType);
    
    List<CommercialProposalAttachment> findByProposalId(Long proposalId);
}
