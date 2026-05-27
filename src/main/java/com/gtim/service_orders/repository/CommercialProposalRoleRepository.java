package com.gtim.service_orders.repository;

import com.gtim.service_orders.entity.CommercialProposal;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gtim.service_orders.entity.CommercialProposalRole;
import java.util.List;
import org.springframework.data.domain.Sort;

public interface CommercialProposalRoleRepository 
extends JpaRepository<CommercialProposalRole, Long> {
    List<CommercialProposalRole> findByProposal(CommercialProposal proposal, Sort sort);
}
