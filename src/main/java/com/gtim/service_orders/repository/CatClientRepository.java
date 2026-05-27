package com.gtim.service_orders.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gtim.service_orders.entity.CatClient;
import org.springframework.data.domain.Sort;

@Repository
public interface CatClientRepository extends JpaRepository<CatClient, Long> {

    List<CatClient> findByActiveTrue(Sort sort);
}
