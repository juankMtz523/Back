package com.gtim.service_orders.mapper;

import org.mapstruct.Mapper;

import com.gtim.service_orders.dto.CatProjectStatusDTO;
import com.gtim.service_orders.entity.CatProjectStatus;

import java.util.List;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CatProjectStatusMapper {

    CatProjectStatusDTO toDto(CatProjectStatus entity);

    CatProjectStatus toEntity(CatProjectStatusDTO dto);

    List<CatProjectStatusDTO> toDtoList(List<CatProjectStatus> entities);
}
