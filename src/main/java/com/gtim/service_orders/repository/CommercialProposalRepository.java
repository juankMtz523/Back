package com.gtim.service_orders.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gtim.service_orders.dto.CommercialProposalListDTO;
import com.gtim.service_orders.entity.CommercialProposal;
import com.gtim.service_orders.entity.ServiceRequest;
import org.springframework.data.domain.Sort;

@Repository
public interface CommercialProposalRepository extends JpaRepository<CommercialProposal, Long> {

    long countByServiceRequest(ServiceRequest serviceRequest);

    List<CommercialProposal> findByServiceRequest(ServiceRequest serviceRequest, Sort sort);

    Page<CommercialProposal> findByServiceRequestAndActiveTrue(ServiceRequest serviceRequest, Pageable pageable);

    @Query("""
                    SELECT new com.gtim.service_orders.dto.CommercialProposalListDTO(
                        cp.id,
                        cp.proposalFolio,
                        cp.status.id,
                        cp.status.name,
                        cp.comments,
                        false
                    )
                    FROM CommercialProposal cp
                    JOIN cp.serviceRequest sr
                    WHERE sr.id = :serviceRequestId
                      AND cp.active = true
            """)
    	List<CommercialProposalListDTO> findByServiceRequest(@Param("serviceRequestId") Long serviceRequestId);

	boolean existsByProposalFolio(String folio);

	
	@Query("""
		    SELECT p
		    FROM CommercialProposal p
		    WHERE p.status.name = :statusName
		      AND p.sentToClientAt IS NOT NULL
		      AND p.expiresAt IS NOT NULL
		      AND p.reminderSentAt IS NULL
		""")
		List<CommercialProposal> findForExpirationReminder(
		        @Param("statusName") String statusName
		);

	

	@Query("""
		    SELECT p
		    FROM CommercialProposal p
		    WHERE p.expiresAt <= :now
		      AND p.status.name = 'En revisión por cliente'
		""")
		List<CommercialProposal> findExpiredProposals(
		        @Param("now") LocalDateTime now
		);

    
    
}
