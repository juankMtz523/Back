package com.gtim.service_orders.mapper;

import org.mapstruct.Mapper;

import com.gtim.service_orders.dto.DocumentDTO;
import com.gtim.service_orders.entity.TrxDocument;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    DocumentDTO toDto(TrxDocument entity);
    TrxDocument toEntity(DocumentDTO dto);
}
