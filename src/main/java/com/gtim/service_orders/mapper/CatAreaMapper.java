package com.gtim.service_orders.mapper;

import org.mapstruct.Mapper;

import com.gtim.service_orders.dto.CatAreaDTO;
import com.gtim.service_orders.entity.CatArea;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CatAreaMapper {

    CatAreaDTO toDto(CatArea entity);
    CatArea toEntity(CatAreaDTO dto);

}
