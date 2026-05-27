package com.gtim.service_orders.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gtim.service_orders.dto.CommercialProposalListDTO;
import com.gtim.service_orders.dto.ProjectAutocompleteDTO;
import com.gtim.service_orders.dto.ProjectDashboardDTO;
import com.gtim.service_orders.entity.ServiceRequest;
import org.springframework.data.domain.Sort;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {

    List<ServiceRequest> findByActiveTrue(Sort sort);

    List<ServiceRequest> findByActiveTrueAndCreatedBy(String createdBy, Sort sort);

    Optional<ServiceRequest> findTopByInternalFolioStartingWithOrderByIdDesc(String prefix, Sort sort);

    @Query("""
		    SELECT new com.gtim.service_orders.dto.ProjectAutocompleteDTO(
		        sr.id,
		        sr.projectName
		    )
		    FROM ServiceRequest sr
		    WHERE sr.active = true
		      AND (
		            LOWER(sr.projectName) LIKE LOWER(CONCAT('%', :query, '%'))
		         OR LOWER(sr.internalFolio) LIKE LOWER(CONCAT('%', :query, '%'))
		      )
		    ORDER BY sr.createdAt DESC, sr.projectName ASC
		""")
    List<ProjectAutocompleteDTO> findByProjectNameOrInternalFolio(
            @Param("query") String query
    );

    @Query("""
		    SELECT new com.gtim.service_orders.dto.ProjectDashboardDTO(
		        sr.id,
		        sr.internalFolio,
		        sr.projectName,
		        gs.id,
		        gs.name,
		        sr.createdAt,
		        a.id,
		        a.name,
		        sr.updatedBy
		    )
		    FROM ServiceRequest sr
		    JOIN sr.generalStatus gs
		    JOIN sr.area a
		    WHERE sr.active = true
		    ORDER BY sr.createdAt DESC
		""")
    Page<ProjectDashboardDTO> findProjectsForDashboard(Pageable pageable);

    boolean existsByProjectNameIgnoreCaseAndClientIdAndAreaIdAndActiveTrue(
            String projectName,
            Long clientId,
            Long areaId
    );

}
