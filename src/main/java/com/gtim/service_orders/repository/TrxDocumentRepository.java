package com.gtim.service_orders.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gtim.service_orders.entity.TrxDocument;

@Repository
public interface TrxDocumentRepository
        extends JpaRepository<TrxDocument, Long> {

    List<TrxDocument> findByRelatedEntityAndRelatedId(
            String relatedEntity,
            Long relatedId
    );
}
