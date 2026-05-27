package com.gtim.service_orders.mapper;

import org.mapstruct.Mapper;

import com.gtim.service_orders.dto.CatClientDTO;
import com.gtim.service_orders.entity.CatClient;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CatClientMapper {
    CatClientDTO toDto(CatClient entity);
    CatClient toEntity(CatClientDTO dto);
}

