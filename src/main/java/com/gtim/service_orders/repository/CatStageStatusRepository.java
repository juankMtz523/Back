package com.gtim.service_orders.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gtim.service_orders.entity.CatStageStatus;
import org.springframework.data.domain.Sort;

@Repository
public interface CatStageStatusRepository extends JpaRepository<CatStageStatus, Long> {
	
	List<CatStageStatus> findByActiveTrue(Sort sort);
}

