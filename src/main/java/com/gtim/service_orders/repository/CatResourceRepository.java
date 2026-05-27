package com.gtim.service_orders.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.gtim.service_orders.entity.CatResource;
import org.springframework.data.domain.Sort;

@Repository
public interface CatResourceRepository extends JpaRepository<CatResource, Long> {

    List<CatResource> findByActiveTrue(Sort sort);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCaseAndActiveTrue(String email);
    
    CatResource findByEmailIgnoreCaseAndActiveFalse(String email);
    
    CatResource findByEmailIgnoreCaseAndActiveTrue(String email);
}
