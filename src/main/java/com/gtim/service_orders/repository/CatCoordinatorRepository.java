package com.gtim.service_orders.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gtim.service_orders.entity.CatCoordinator;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface CatCoordinatorRepository extends JpaRepository<CatCoordinator, Long> {

    List<CatCoordinator> findByActiveTrue(Sort sort);

    @Query("SELECT c from CatCoordinator c where UPPER(c.name) = UPPER(:name)")
    CatCoordinator findByNameToUpperCase(@Param("name") String name);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndActiveTrue(String email);
    
    CatCoordinator findByEmailIgnoreCaseAndActiveFalse(String email);
    
    CatCoordinator findByEmailIgnoreCaseAndActiveTrue(String email);
}
