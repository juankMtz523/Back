package com.gtim.service_orders.mapper;

import org.mapstruct.Mapper;

import com.gtim.service_orders.dto.CatProposalStatusDTO;
import com.gtim.service_orders.entity.CatProposalStatus;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CatProposalStatusMapper {
    CatProposalStatusDTO toDto(CatProposalStatus entity);
    CatProposalStatus toEntity(CatProposalStatusDTO dto);
}
