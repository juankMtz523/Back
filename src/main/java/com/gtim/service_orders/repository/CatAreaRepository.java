package com.gtim.service_orders.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gtim.service_orders.entity.CatArea;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface CatAreaRepository extends JpaRepository<CatArea, Long> {

	List<CatArea> findByActiveTrue(Sort sort);
        
        List<CatArea> findByTipoAreaId(Long id, Sort sort);

        @Query("SELECT a from CatArea a where UPPER(a.description) = UPPER(:description)")
        CatArea findByDesciptionToUpperCase(@Param("description") String description);
}
