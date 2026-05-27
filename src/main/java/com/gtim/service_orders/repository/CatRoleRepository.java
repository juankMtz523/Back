package com.gtim.service_orders.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gtim.service_orders.entity.CatRole;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface CatRoleRepository extends JpaRepository<CatRole, Long> {
	

    List<CatRole> findByActiveTrue(Sort sort);
    
    List<CatRole> findByRolSystemFalse(Sort sort);

    @Query("SELECT r from CatRole r where UPPER(r.description) = UPPER(:description)")
    CatRole findByDesciptionToUpperCase(@Param("description") String description);
}
