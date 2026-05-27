package com.gtim.service_orders.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gtim.service_orders.entity.CatProjectStatus;
import org.springframework.data.domain.Sort;

@Repository
public interface CatProjectStatusRepository extends JpaRepository<CatProjectStatus, Long> {
	
	List<CatProjectStatus> findByActiveTrue(Sort sort);

	Optional<CatProjectStatus> findByName(String name, Sort sort);
}
