package com.gtim.service_orders.mapper;

import org.mapstruct.*;

import com.gtim.service_orders.dto.CatCoordinatorDTO;
import com.gtim.service_orders.entity.CatCoordinator;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CatCoordinatorMapper {

    @Mapping(source = "engineering.id", target = "engineeringId")
    @Mapping(source = "engineering.description", target = "engineeringName")
    CatCoordinatorDTO toDto(CatCoordinator entity);

    @Mapping(source = "engineeringId", target = "engineering.id")
    CatCoordinator toEntity(CatCoordinatorDTO dto);
}
