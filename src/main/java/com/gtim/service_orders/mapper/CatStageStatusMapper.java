package com.gtim.service_orders.mapper;

import org.mapstruct.*;

import com.gtim.service_orders.dto.CatStageStatusDTO;
import com.gtim.service_orders.entity.CatStageStatus;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CatStageStatusMapper {

    CatStageStatusDTO toDto(CatStageStatus entity);

    CatStageStatus toEntity(CatStageStatusDTO dto);

    List<CatStageStatusDTO> toDtoList(List<CatStageStatus> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(CatStageStatusDTO dto, @MappingTarget CatStageStatus entity);
}
