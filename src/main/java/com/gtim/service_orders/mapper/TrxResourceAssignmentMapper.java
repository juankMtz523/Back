package com.gtim.service_orders.mapper;

import org.mapstruct.Mapper;
import com.gtim.service_orders.dto.ResourceAssignmentDTO;
import com.gtim.service_orders.entity.TrxResourceAssignment;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.BeanMapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TrxResourceAssignmentMapper {
    
    ResourceAssignmentDTO toDto(TrxResourceAssignment entity);
    TrxResourceAssignment toEntity(ResourceAssignmentDTO dto);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(ResourceAssignmentDTO dto, @MappingTarget TrxResourceAssignment entity);    
}
