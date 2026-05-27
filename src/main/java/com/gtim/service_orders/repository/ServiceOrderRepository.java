package com.gtim.service_orders.repository;

import com.gtim.service_orders.entity.TrxServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceOrderRepository extends JpaRepository<TrxServiceOrder, Long> {

    TrxServiceOrder findByProposalId(Long proposalId);

    boolean existsByProposalId(Long proposalId);
}
