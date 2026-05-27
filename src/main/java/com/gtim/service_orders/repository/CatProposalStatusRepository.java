package com.gtim.service_orders.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gtim.service_orders.entity.CatProposalStatus;
import org.springframework.data.domain.Sort;

@Repository
public interface CatProposalStatusRepository extends JpaRepository<CatProposalStatus, Long> {
	List<CatProposalStatus> findByActiveTrue(Sort sort);

	Optional<CatProposalStatus> findByName(String name, Sort sort);
}

