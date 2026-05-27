package com.gtim.service_orders.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gtim.service_orders.entity.CatHorasasginacion;

@Repository
public interface CatHoraAsignacionRepository extends JpaRepository<CatHorasasginacion, Long> {
    
}
